import java.io.File;
import java.util.List;


/**
 * 测试类
 * shi
 * @author shi
 *
 */
public class TestFileUtil {

		 
		 
		public static void main(String[] args) {
		/**
		* apache common-net实现的
		*/
			FtpTools ftpUtil = new FtpTools("150970520","lovechen66", "192.168.8.242", 21);
//			Ziputil ziputil = new Ziputil();
		 
		//   /test要下载的文件夹。
		ftpUtil.down("/Shared Folder/csv/","/Users/wanghuachen/Downloads");
//		List<String> lis = ftpUtil.list;


//		for(String str : lis) {
//		    System.out.println(str);
//		    String zip = "/Users/wanghuachen/Downloads"+str;
//		    System.out.println("这是解压文件的路径："+zip);
//	    	List<File> list = ziputil.upzipFile(zip, "/Users/wanghuachen/Downloads");
//	    	for(File file : list){
//	    		System.out.println(file.getName());
//	    	}
//	    	//deletefile(zip);
//		}


		 
		 
		}
}
		 
		


