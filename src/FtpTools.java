import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;
import org.apache.log4j.Logger;

/**
 * ���õ���apache commons-net�ܰ��е�ftp������ʵ�ֵ�
 * 
 * @author shi
 * 
 */
public class FtpTools {
	private String username;
	private String password;
	private String ftpHostName;
	private int port = 21;
	private FTPClient ftpClient = new FTPClient();
	private FileOutputStream fos = null;
	public List<String> list = new ArrayList<String>();
	private Logger logger = Logger.getLogger(FtpTools.class);

	public FtpTools(String username, String password, String ftpHostName, int port) {
		super();
		this.username = username;
		this.password = password;
		this.ftpHostName = ftpHostName;
		this.port = port;
	}

	/**
	 * ��������b
	 */
	private void connect() {
		try {
			logger.debug("��ʼ����");
			// ����
			ftpClient.connect(ftpHostName, port);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
			}
			// ��¼
			ftpClient.login(username, password);
			ftpClient.setBufferSize(256);

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			ftpClient.setControlEncoding("utf8");
			logger.debug("��¼�ɹ���");
			logger.debug("��ʼ��¼��");
		} catch (SocketException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}

	}

	/**
	 * �ر����������
	 * 
	 * @param fos
	 */
	private void close(FileOutputStream fos) {
		try {
			if (fos != null) {
				fos.close();
			}

			ftpClient.logout();
			logger.info("�˳���¼");
			ftpClient.disconnect();
			logger.info("�ر�����");
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * �����ļ�
	 * 
	 * @param ftpFileName
	 * @param localDir
	 */
	public void down(String ftpFileName, String localDir) {
		connect();
		downFileOrDir(ftpFileName, localDir);
		close(fos);
	}

	private void downFileOrDir(String ftpFileName, String localDir) {
		try {
			File file = new File(ftpFileName);

			File temp = new File(localDir);

			if (!temp.exists()) {
				temp.mkdirs();
			}
			// �ж��Ƿ���Ŀ¼
			if (isDir(ftpFileName)) {
				String[] names = ftpClient.listNames();
				for (int i = 0; i < names.length; i++) {
					System.out.println("---------------"+names[i] + "-------------------");
					list.add(names[i]);
					if (isDir(names[i])) {
						downFileOrDir(ftpFileName + '/' + names[i], localDir + File.separator + names[i]);
						ftpClient.changeToParentDirectory();
					} else {
						File localfile = new File(localDir + File.separator + names[i]);
						if (!localfile.exists()) {
							fos = new FileOutputStream(localfile);
							ftpClient.retrieveFile(names[i], fos);

						} else {
							logger.debug("��ʼɾ���ļ�");
							file.delete();
							logger.debug("�ļ��Ѿ�ɾ��");
							fos = new FileOutputStream(localfile);
							ftpClient.retrieveFile(ftpFileName, fos);

						}

					}
				}
			} else {

				File localfile = new File(localDir + File.separator + file.getName());
				if (!localfile.exists()) {
					fos = new FileOutputStream(localfile);
					ftpClient.retrieveFile(ftpFileName, fos);

				} else {
					logger.debug("��ʼɾ���ļ�");
					file.delete();
					logger.debug("�ļ��Ѿ�ɾ��");
					fos = new FileOutputStream(localfile);
					ftpClient.retrieveFile(ftpFileName, fos);
				}
				ftpClient.changeToParentDirectory();

			}

			logger.info("���سɹ���");
		} catch (SocketException e) {
			logger.error("����ʧ�ܣ�", e);
		} catch (IOException e) {
			logger.error("����ʧ�ܣ�", e);
		}

	}

	// �ж��Ƿ���Ŀ¼
	public boolean isDir(String fileName) {
		try {
			// �л�Ŀ¼������ǰ��Ŀ¼�򷵻�true,���򷵻�false��
			boolean falg = ftpClient.changeWorkingDirectory(fileName);
			return falg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
		}

		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtpHostName() {
		return ftpHostName;
	}

	public void setFtpHostName(String ftpHostName) {
		this.ftpHostName = ftpHostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


}