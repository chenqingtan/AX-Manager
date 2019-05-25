package pangolin.ax.plus.XUtil;
import android.content.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import android.widget.*;
import android.support.v7.app.*;
import android.util.*;
import pangolin.ax.plus.XAdapter.*;

public class XZipList
{
	private static ZipFile zip;
	private static Stack<String> path;
    private static int dep;
	public static HashMap<String,byte[]> zipEnties;
	public static Tree GetList(List<String> mList, String Path,XFileAdapter Adapter) throws IOException
	{
		path = null;
		dep = 0;
		mList.clear();
		mList.add(new File(Path).getParent());
		zipEnties = new HashMap<String,byte[]>();
		zip = new ZipFile(Path);
		Enumeration<? extends ZipEntry> enum= zip.entries();
		while (enum.hasMoreElements())
		{
			ZipEntry entry=enum.nextElement();
			zipEnties.put(entry.getName(), null);
		}
		Tree Tree=new Tree(zipEnties.keySet());
		mList.addAll(Tree.list());
		Adapter.setmList(mList);
		Adapter.setModeApk(true);
		Adapter.setTree(Tree);
		Adapter.notifyDataSetChanged();
		return Tree;
	}
	public static void Push(List<String> mList,String Name,Tree Tree,XFileAdapter Adapter)
	{
		Tree.push(Name);
		mList.clear();
		mList.add("");
		mList.addAll(Tree.list());
		Adapter.setmList(mList);
		Adapter.notifyDataSetChanged();
	}
	public static void Pop(List<String> mList,Tree Tree,XFileAdapter Adapter)
	{
		mList.clear();
		mList.add("");
		mList.addAll(Tree.list());
		Adapter.setmList(mList);
		Adapter.notifyDataSetChanged();
	}
	public static class Tree
	{
        List<Map<String,String>> node;
        Comparator<String> sortByType=new Comparator<String>(){
            public int compare(String a, String b)
			{
                if (isDirectory(a) && !isDirectory(b))
				{
                    return -1;
                }
                if (!isDirectory(a) && isDirectory(b))
				{
                    return 1;
                }
                return a.toLowerCase().compareTo(b.toLowerCase());
            }
        };

        public Tree(Set<String> names)
		{
            if (path == null)
			{
                path = new Stack<String>();
                dep = 0;
            }
            HashMap<String,byte[]> zipEnties=XZipList.zipEnties;
            node = new ArrayList<Map<String,String>>();
            for (String name :names)
			{
                String[] token=name.split("/");
                String tmp="";
                for (int i=0,len=token.length;i < len;i++)
				{
                    String value=token[i];
                    if (i >= node.size())
					{
                        Map<String,String> map=new HashMap<String,String>();
                        if (zipEnties.containsKey(tmp + value)
							&& i + 1 == len)
						{
                            map.put(tmp + value, tmp);
                        }
						else
						{
                            map.put(tmp + value + "/", tmp);
                        }
                        node.add(map);
                        tmp += value + "/";
                    }
					else
					{
                        Map<String,String> map=node.get(i);
                        if (zipEnties.containsKey(tmp + value)
							&& i + 1 == len)
						{
                            map.put(tmp + value, tmp);
                        }
						else
						{
                            map.put(tmp + value + "/", tmp);
                        }
                        tmp += value + "/";
                    }
                }
            }
        }
        private List<String> list(String parent)
		{
            Map<String,String> map=null;
            List<String> str=new ArrayList<String>();
            while (dep >= 0 && node.size() > 0)
			{
                map = node.get(dep);
                if (map != null)
				{
                    break;
                }
                pop();
            }
            if (map == null)
			{
                return str;
            }
            for (String key :map.keySet())
			{
                if (parent.equals(map.get(key)))
				{
                    int index;
                    if (key.endsWith("/"))
					{
                        index = key.lastIndexOf("/", key.length() - 2);
                    }
					else
					{
                        index = key.lastIndexOf("/");
                    }
                    if (index != -1)
                        key = key.substring(index + 1);
                    str.add(key);
                }
            }
            Collections.sort(str, sortByType);

            return str;
        }

        public void addNode(String name)
		{
            Map<String,String> map=node.get(dep);
            map.put(getCurPath() + name, getCurPath());
        }

        public void deleteNode(String name)
		{
            Map<String,String> map=node.get(dep);
            map.remove(getCurPath() + name);
        }

        public List<String> list()
		{
            return list(getCurPath());
        }
        public void push(String name)
		{
            dep++;
            path.push(name);
        }
        public String pop()
		{
            if (dep > 0)
			{
                dep--;
                return path.pop();
            }
            return null;
        }
        public String getCurPath()
		{
            return join(path, "/");
        }
        public boolean isDirectory(String name)
		{
            return name.endsWith("/");
        }

        private String join(Stack<String> stack, String d)
		{
            StringBuilder sb=new StringBuilder("");
            for (String s: stack)
			{
                sb.append(s);
            }
            return sb.toString();
        }
		public byte[] readEntryForZip(String name)
		{
			ZipEntry zipEntry=zip.getEntry(name);
			if (zipEntry != null)
			{
				ByteArrayOutputStream baos= new ByteArrayOutputStream(8 * 1024);
				byte[] buf=new byte[4 * 1024];
				try
				{
					InputStream in=zip.getInputStream(zipEntry);
					int count;
					while ((count = in.read(buf, 0, buf.length)) != -1)
					{
						baos.write(buf, 0, count);
					}
					in.close();
					baos.close();
				}
				catch (IOException io)
				{}

				return baos.toByteArray();
			}
			return null;
		}
		public ZipFile getZip()
		{
			if(zip==null)throw new AndroidRuntimeException("Null");
			return zip;
		}
    }


}
