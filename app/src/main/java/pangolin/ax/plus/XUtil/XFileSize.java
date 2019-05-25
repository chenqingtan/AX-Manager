package pangolin.ax.plus.XUtil;


import java.io.*;
import java.text.*;

public class XFileSize
{
	public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
	public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
	public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
	public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
	public static String convertBytesLength(long len){
		if(len<1024){
			return len+"B";
		}
		if(len<1024*1024){
			return String.format("%.2f%s",(len/1024.0),"K");
		}
		if(len<1024*1024*1024)
			return String.format("%.2f%s",(len/(1024*1024.0)),"M");
		return String.format("%.2f%s",(len/(1024*1024*1024.0)),"G");
	}
	/**
	 * 调用此方法自动计算指定文件或指定文件夹的大小
	 *
	 * @param filePath 文件路径
	 * @return 计算好的带B、KB、MB、GB的字符串
	 */
	public static String GetFileSize(String Path){
		try
		{
			String line=null;
			StringBuilder sb = new StringBuilder();
			Process p= Runtime.getRuntime().exec("du -sh " + Path);
			BufferedReader	bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString().replace(Path,"").trim();
		}
		catch (Exception e)
		{}

		return null;
	}
	 
	public static double getFileOrFilesSize ( String filePath, int sizeType )
	{
		File file = new File ( filePath );
		long blockSize = 0;
		try
		{
			if ( file.isDirectory ( ) )
			{
				blockSize = getFileSizes ( file );
			}
			else
			{
				blockSize = getFileSize ( file );
			}
		}
		catch (Exception e)
		{
			e.printStackTrace ( );
		}
		return FormetFileSize ( blockSize, sizeType );
	}
	public static String getAutoFileOrFilesSize ( File file )
	{
		long blockSize = 0;
		try
		{
			if ( file.isDirectory ( ) )
			{
				blockSize = getFileSizes ( file );
			}
			else
			{
				blockSize = getFileSize ( file );
			}
		}
		catch (Exception e)
		{
			e.printStackTrace ( );
		}
		return FormetFileSize ( blockSize );
	}
	public static String getAutoFileOrFilesSize ( String filePath )
	{
		File file = new File ( filePath );
		long blockSize = 0;
		try
		{
			if ( file.isDirectory ( ) )
			{
				blockSize = getFileSizes ( file );
			}
			else
			{
				blockSize = getFileSize ( file );
			}
		}
		catch (Exception e)
		{
			e.printStackTrace ( );
		}
		return FormetFileSize ( blockSize );
	}



	/**
	 * 调用此方法自动计算指定文件的大小
	 *
	 * @param filePath 文件路径
	 * @return 计算好的带B、KB、MB、GB的字符串
	 */
	public static String getAutoFileOrFileSize ( File file )
	{
		long blockSize = 0;
		try
		{
			blockSize = getFileSize ( file );
		}
		catch (Exception e)
		{
			e.printStackTrace ( );
		}
		return FormetFileSize ( blockSize );
	}
	public static String getAutoFileOrFileSize ( String filePath )
	{
		File file = new File ( filePath );
		long blockSize = 0;
		try
		{
			blockSize = getFileSize ( file );
		}
		catch (Exception e)
		{
			e.printStackTrace ( );
		}
		return FormetFileSize ( blockSize );
	}

	/**
	 * 获取指定文件大小
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static long getFileSize ( File file ) throws Exception
	{
		long size = 0;
		if ( file.exists ( ) )
		{
			FileInputStream fis = null;
			fis = new FileInputStream ( file );
			size = fis.available ( );
		}
		else
		{
			file.createNewFile ( );					
		}
		return size;
	}
	private static long getFileSizes ( File f ) throws Exception
	{
		long size = 0;
		File flist[] = f.listFiles ( );
		for ( int i = 0; i < flist.length; i++ )
		{
			if ( flist [ i ].isDirectory ( ) )
			{
				size = size + getFileSizes ( flist [ i ] );
			}
			else
			{
				size = size + getFileSize ( flist [ i ] );
			}
		}
		return size;
	}
	private static String FormetFileSize ( long fileS )
	{
		DecimalFormat df = new DecimalFormat ( "#.00" );
		String fileSizeString = "";
		String wrongSize = "0B";
		if ( fileS == 0 )
		{
			return wrongSize;
		}
		if ( fileS < 1024 )
		{
			fileSizeString = df.format ( (double) fileS ) + "B";
		}
		else if ( fileS < 1048576 )
		{
			fileSizeString = df.format ( (double) fileS / 1024 ) + "KB";
		}
		else if ( fileS < 1073741824 )
		{
			fileSizeString = df.format ( (double) fileS / 1048576 ) + "MB";
		}
		else
		{
			fileSizeString = df.format ( (double) fileS / 1073741824 ) + "GB";
		}
		return fileSizeString;
	}
	private static double FormetFileSize ( long fileS, int sizeType )
	{
		DecimalFormat df = new DecimalFormat ( "#.00" );
		double fileSizeLong = 0;
		switch ( sizeType )
		{
			case SIZETYPE_B:
				fileSizeLong = Double.valueOf ( df.format ( (double) fileS ) );
				break;
			case SIZETYPE_KB:
				fileSizeLong = Double.valueOf ( df.format ( (double) fileS / 1024 ) );
				break;
			case SIZETYPE_MB:
				fileSizeLong = Double.valueOf ( df.format ( (double) fileS / 1048576 ) );
				break;
			case SIZETYPE_GB:
				fileSizeLong = Double.valueOf ( df.format ( (double) fileS / 1073741824 ) );
				break;
			default:
				break;
		}
		return fileSizeLong;
	}
}

