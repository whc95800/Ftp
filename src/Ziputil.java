import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
 
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class Ziputil {
	
     private static byte[] _byte = new byte[1024];
	
	private static final String ENCODE_UTF_8 = "UTF-8";

	/**
     * ��ѹ��ZIP�ļ�����ZIP�ļ�������ݽ�ѹ��targetDIRĿ¼��
     * @param zipName ����ѹ����ZIP�ļ���
     * @param targetBaseDirName  Ŀ��Ŀ¼
     */
    public static List<File> upzipFile(String zipPath, String descDir) {
//    	ZipFile zip = new ZipFile(zipFile,Charset.forName("GBK"));//��������ļ�������
    	System.setProperty("sun.zip.encoding", System.getProperty("sun.jnu.encoding")); //��ֹ�ļ�����������ʱ����
        return upzipFile( new File(zipPath) , descDir ) ;
    }
    
    /**
     * ��.zip�ļ����н�ѹ��
     * @param zipFile  ��ѹ���ļ�
     * @param descDir  ѹ����Ŀ���ַ���磺D:\\���� �� /mnt/d/����
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<File> upzipFile(File zipFile, String descDir) {
        List<File> _list = new ArrayList<File>() ;
        
        System.setProperty("sun.file.encoding", System.getProperty("sun.jnu.encoding")); //��ֹ�ļ�����������ʱ����
      System.out.println(System.getProperty("sun.zip.encoding")); //ZIP���뷽ʽ
        System.out.println(System.getProperty("sun.jnu.encoding")); //��ǰ�ļ����뷽ʽ
        System.out.println(System.getProperty("file.encoding")); //����ǵ�ǰ�ļ����ݱ��뷽ʽ
        try {
        	if(!zipFile.exists()){
        		
        		System.out.println("��ѹʧ�ܣ��ļ� " + zipFile + " ������!");
        		return _list ;
        	}
            ZipFile _zipFile = new ZipFile(zipFile , ENCODE_UTF_8) ;
            for( Enumeration entries = _zipFile.getEntries() ; entries.hasMoreElements() ; ){
                ZipEntry entry = (ZipEntry)entries.nextElement() ;
                File _file = new File(descDir + File.separator + entry.getName()) ;
                if( entry.isDirectory() ){
                    _file.mkdirs() ;
                }else{
                    File _parent = _file.getParentFile() ;
                    if( !_parent.exists() ){
                        _parent.mkdirs() ;
                    }
                    InputStream _in = _zipFile.getInputStream(entry);
                    OutputStream _out = new FileOutputStream(_file) ;
                    int len = 0 ;
                    while( (len = _in.read(_byte)) > 0){
                        _out.write(_byte, 0, len);
                    }
                    _in.close(); 
                    _out.flush();
                    _out.close();
                    _list.add(_file) ;
                }
            }
        } catch (IOException e) {
        }
        return _list ;
    }
    	
    /**
     * ����ʱ���ɵ��ļ��к��ļ����µ��ļ�����ɾ��
     */
    public static void deletefile(String delpath) {
        try {
            File file = new File(delpath);
            if (!file.isDirectory()) {//�ж��ǲ���һ��Ŀ¼
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + File.separator + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        deletefile(delpath + File.separator + filelist[i]);//�ݹ�ɾ��
                    }
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
    	String zip = "D://��˾�ļ�/Test";
    	List<File> list = upzipFile(zip, "D://��˾�ļ�/Test/jieya");
    	for(File file : list){
    		System.out.println(file.getName());
    	}
    	deletefile(zip);
    }

}