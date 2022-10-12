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
 * 采用的是apache commons-net架包中的ftp工具类实现的
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
	 * 建立连接b
	 */
	private void connect() {
		try {
			logger.debug("开始连接");
			// 连接
			ftpClient.connect(ftpHostName, port);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
			}
			// 登录
			ftpClient.login(username, password);
			ftpClient.setBufferSize(256);

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			ftpClient.setControlEncoding("utf8");
			logger.debug("登录成功！");
			logger.debug("开始登录！");
		} catch (SocketException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}

	}

	/**
	 * 关闭输入输出流
	 * 
	 * @param fos
	 */
	private void close(FileOutputStream fos) {
		try {
			if (fos != null) {
				fos.close();
			}

			ftpClient.logout();
			logger.info("退出登录");
			ftpClient.disconnect();
			logger.info("关闭连接");
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 下载文件
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
			// 判断是否是目录
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
							logger.debug("开始删除文件");
							file.delete();
							logger.debug("文件已经删除");
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
					logger.debug("开始删除文件");
					file.delete();
					logger.debug("文件已经删除");
					fos = new FileOutputStream(localfile);
					ftpClient.retrieveFile(ftpFileName, fos);
				}
				ftpClient.changeToParentDirectory();

			}

			logger.info("下载成功！");
		} catch (SocketException e) {
			logger.error("连接失败！", e);
		} catch (IOException e) {
			logger.error("下载失败！", e);
		}

	}

	// 判断是否是目录
	public boolean isDir(String fileName) {
		try {
			// 切换目录，若当前是目录则返回true,否则返回false。
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