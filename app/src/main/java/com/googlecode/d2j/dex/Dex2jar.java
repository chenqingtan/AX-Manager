/*
 * dex2jar - Tools to work with android .dex and java .class files
 * Copyright (c) 2009-2012 Panxiaobo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.d2j.dex;

import com.googlecode.d2j.converter.*;
import com.googlecode.d2j.node.*;
import com.googlecode.d2j.reader.*;
import com.googlecode.d2j.reader.zip.*;
import com.googlecode.dex2jar.ir.*;
import com.googlecode.dex2jar.ir.stmt.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import org.objectweb.asm.*;

public class Dex2jar
{
	public static Dex2jar from(byte[] in) throws IOException
	{
		return from(new DexFileReader(ZipUtil.readDex(in)));
	}

	public static Dex2jar from(ByteBuffer in) throws IOException
	{
		return from(new DexFileReader(in));
	}

	public static Dex2jar from(BaseDexFileReader reader)
	{
		return new Dex2jar(reader);
	}

	public static Dex2jar from(InputStream in) throws IOException
	{
		return from(new DexFileReader(in));
	}


	private DexExceptionHandler exceptionHandler;

	final private BaseDexFileReader reader;
	private int readerConfig;
	private int v3Config;
	private JarOutputStream zos;
	private Dex2jar(BaseDexFileReader reader)
	{
		super();
		this.reader = reader;
		readerConfig |= DexFileReader.SKIP_DEBUG;
	}

	private void doTranslate(final File dist) throws IOException
	{

		DexFileNode fileNode = new DexFileNode();
		try
		{
			reader.accept(fileNode, readerConfig | DexFileReader.IGNORE_READ_EXCEPTION);
		}
		catch (Exception ex)
		{
			exceptionHandler.handleFileException(ex);
		}
		ClassVisitorFactory cvf = new ClassVisitorFactory() {
			@Override
			public ClassVisitor create(final String name)
			{
				final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				final LambadaNameSafeClassAdapter rca = new LambadaNameSafeClassAdapter(cw);
				return new ClassVisitor(Opcodes.ASM5, rca) {
					@Override
					public void visitEnd()
					{
						super.visitEnd();
						String className = rca.getClassName();
						byte[] data;
						try
						{
							// FIXME handle 'java.lang.RuntimeException: Method code too large!'
							data = cw.toByteArray();
						}
						catch (Exception ex)
						{
							System.err.println(String.format("ASM fail to generate .class file: %s", className));
							exceptionHandler.handleFileException(ex);
							return;
						}
						try
						{
							zos.putNextEntry(new ZipEntry(className + ".class"));
							zos.write(data);
							zos.closeEntry();
						}
						catch (Exception e)
						{
							e.printStackTrace(System.err);
						}
					}
				};
			}
		};

		new ExDex2Asm(exceptionHandler) {
			public void convertCode(DexMethodNode methodNode, MethodVisitor mv)
			{
				if ((readerConfig & DexFileReader.SKIP_CODE) != 0 && methodNode.method.getName().equals("<clinit>"))
				{
					// also skip clinit
					return;
				}
				super.convertCode(methodNode, mv);
			}

			@Override
			public void optimize(IrMethod irMethod)
			{
				T_cleanLabel.transform(irMethod);
				if (0 != (v3Config & V3.TOPOLOGICAL_SORT))
				{
					// T_topologicalSort.transform(irMethod);
				}
				T_deadCode.transform(irMethod);
				T_removeLocal.transform(irMethod);
				T_removeConst.transform(irMethod);
				T_zero.transform(irMethod);
				if (T_npe.transformReportChanged(irMethod))
				{
					T_deadCode.transform(irMethod);
					T_removeLocal.transform(irMethod);
					T_removeConst.transform(irMethod);
				}
				T_new.transform(irMethod);
				T_fillArray.transform(irMethod);
				T_agg.transform(irMethod);
				T_multiArray.transform(irMethod);
				T_voidInvoke.transform(irMethod);
				if (0 != (v3Config & V3.PRINT_IR))
				{
					int i = 0;
					for (Stmt p : irMethod.stmts)
					{
						if (p.st == Stmt.ST.LABEL)
						{
							LabelStmt labelStmt = (LabelStmt) p;
							labelStmt.displayName = "L" + i++;
						}
					}
					System.out.println(irMethod);
				}
				T_type.transform(irMethod);
				T_unssa.transform(irMethod);
				T_ir2jRegAssign.transform(irMethod);
				T_trimEx.transform(irMethod);
			}

			@Override
			public void ir2j(IrMethod irMethod, MethodVisitor mv)
			{
				new IR2JConverter(0 != (V3.OPTIMIZE_SYNCHRONIZED & v3Config)).convert(irMethod, mv);
			}
		}.convertDex(fileNode, cvf);

	}

	public DexExceptionHandler getExceptionHandler()
	{
		return exceptionHandler;
	}

	public BaseDexFileReader getReader()
	{
		return reader;
	}

	public Dex2jar reUseReg(boolean b)
	{
		if (b)
		{
			this.v3Config |= V3.REUSE_REGISTER;
		}
		else
		{
			this.v3Config &= ~V3.REUSE_REGISTER;
		}
		return this;
	}

	public Dex2jar topoLogicalSort(boolean b)
	{
		if (b)
		{
			this.v3Config |= V3.TOPOLOGICAL_SORT;
		}
		else
		{
			this.v3Config &= ~V3.TOPOLOGICAL_SORT;
		}
		return this;
	}

	public Dex2jar noCode(boolean b)
	{
		if (b)
		{
			this.readerConfig |= DexFileReader.SKIP_CODE | DexFileReader.KEEP_CLINIT;
		}
		else
		{
			this.readerConfig &= ~(DexFileReader.SKIP_CODE | DexFileReader.KEEP_CLINIT);
		}
		return this;
	}

	public Dex2jar optimizeSynchronized(boolean b)
	{
		if (b)
		{
			this.v3Config |= V3.OPTIMIZE_SYNCHRONIZED;
		}
		else
		{
			this.v3Config &= ~V3.OPTIMIZE_SYNCHRONIZED;
		}
		return this;
	}

	public Dex2jar printIR(boolean b)
	{
		if (b)
		{
			this.v3Config |= V3.PRINT_IR;
		}
		else
		{
			this.v3Config &= ~V3.PRINT_IR;
		}
		return this;
	}

	public Dex2jar reUseReg()
	{
		this.v3Config |= V3.REUSE_REGISTER;
		return this;
	}

	public Dex2jar optimizeSynchronized()
	{
		this.v3Config |= V3.OPTIMIZE_SYNCHRONIZED;
		return this;
	}

	public Dex2jar printIR()
	{
		this.v3Config |= V3.PRINT_IR;
		return this;
	}

	public Dex2jar topoLogicalSort()
	{
		this.v3Config |= V3.TOPOLOGICAL_SORT;
		return this;
	}

	public void setExceptionHandler(DexExceptionHandler exceptionHandler)
	{
		this.exceptionHandler = exceptionHandler;
	}

	public Dex2jar skipDebug(boolean b)
	{
		if (b)
		{
			this.readerConfig |= DexFileReader.SKIP_DEBUG;
		}
		else
		{
			this.readerConfig &= ~DexFileReader.SKIP_DEBUG;
		}
		return this;
	}

	public Dex2jar skipDebug()
	{
		this.readerConfig |= DexFileReader.SKIP_DEBUG;
		return this;
	}
	public void to(File file) throws IOException
	{
		zos = new JarOutputStream(new FileOutputStream(file));
		doTranslate(file);
		if (zos != null)
		{
			zos.close();
		}
	}
	public byte[] toByteArray(InputStream in) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}
	public Dex2jar withExceptionHandler(DexExceptionHandler exceptionHandler)
	{
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	public Dex2jar skipExceptions(boolean b)
	{
		if (b)
		{
			this.readerConfig |= DexFileReader.SKIP_EXCEPTION;
		}
		else
		{
			this.readerConfig &= ~DexFileReader.SKIP_EXCEPTION;
		}
		return this;
	}
}
