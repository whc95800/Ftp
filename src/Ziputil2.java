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
	 * �������sourceFilePathĿ¼�µ�Դ�ļ��������fileName���Ƶ�zip�ļ�������ŵ�zipFilePath·����
	 * (��ָ���ļ����µ������ļ�Ŀ¼���ļ���ѹ����ָ���ļ�����)
	 * @param sourceFilePath
	 *            :��ѹ�����ļ�·��
	 * @param zipFilePath
	 *            :ѹ������·��
	 * @param fileName
	 *            :ѹ�����ļ�������
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
				System.out.println(zipFilePath + "Ŀ¼�´�������Ϊ:" + fileName
						+ ".zip" + "�Ĵ���ļ�.");
			} else { 
				if(!zipFile.exists()){
					zipFile.getParentFile().mkdirs();
				}
				fos = new FileOutputStream(zipFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				byte[] bufs = new byte[1024 * 1024];
				for (int i = 0; i < listFile.size(); i++) {
					try {
						//����ZIPʵ�壬����ӽ�ѹ����
						ZipEntry zipEntry = new ZipEntry(listFile.get(i));
						zos.putNextEntry(zipEntry);
						// ��ȡ��ѹ�����ļ���д��ѹ������
						fis = new FileInputStream(listFile.get(i));
						bis = new BufferedInputStream(fis, 1024 * 1024);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 1024)) != -1) {
							zos.write(bufs, 0, read);
						}
					} catch (Exception e) {
						//logger.error("�ļ���ȡ��������");
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
			// �ر���
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
		String sourceFilePath = "D://��˾�ļ�/Test";
		String zipFilePath = "D://��˾�ļ�/Test/jieya";
		String fileName = "zipFilej";
		fileToZip(sourceFilePath,zipFilePath, fileName);
	}
}
