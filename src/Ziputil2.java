import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 
import org.apache.log4j.Logger;
 
public class Ziputil2 {
 
	private  final Logger logger = Logger.getLogger(getClass());
 
	public static List<String> listFile = new ArrayList<String>();
	
 
	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 * (把指定文件夹下的所有文件目录和文件都压缩到指定文件夹下)
	 * @param sourceFilePath
	 *            :待压缩的文件路径
	 * @param zipFilePath
	 *            :压缩后存放路径
	 * @param fileName
	 *            :压缩后文件的名称
	 * @return
	 */
	public static  boolean fileToZip(String sourceFilePath,String zipFilePath, String fileName) {
		boolean flag = false;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		getFile(sourceFilePath);
		try {
			File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
			if (zipFile.exists()) {
				System.out.println(zipFilePath + "目录下存在名字为:" + fileName
						+ ".zip" + "的打包文件.");
			} else { 
				if(!zipFile.exists()){
					zipFile.getParentFile().mkdirs();
				}
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				byte[] bufs = new byte[1024 * 1024];
				for (int i = 0; i < listFile.size(); i++) {
					try {
						//创建ZIP实体，并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(listFile.get(i));
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(listFile.get(i));
						bis = new BufferedInputStream(fis, 1024 * 1024);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 1024)) != -1) {
							zos.write(bufs, 0, read);
						}
					} catch (Exception e) {
						//logger.error("文件读取处理有误");
						e.printStackTrace();
					}
					
				}
				flag = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			// 关闭流
			try {
				if (null != bis)
					bis.close();
				if (null != zos)
					zos.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return flag;
	}
	public static void getFile(String path) {
		File file = new File(path);
		File[] tempList = file.listFiles();
		for (File f : tempList) {
			if (f.isFile()) {
				listFile.add(f.getPath());
				System.out.println(f.getPath());
				continue;
			}
			if (f.isDirectory()) {
				getFile(f.getPath());
			}
		}
	}
	public static void main(String[] args) {
		String sourceFilePath = "D://公司文件/Test";
		String zipFilePath = "D://公司文件/Test/jieya";
		String fileName = "zipFilej";
		fileToZip(sourceFilePath,zipFilePath, fileName);
	}
}
