package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName: AESUtil
 * @Description: AES加密工具类*
 */
public class AESUtil {

	public static String DEFALUT_KEY = "CMBCROBOT";

	/**
	 * 加密
	 * 
	 * @param strKey
	 *            加密key
	 * @param strIn
	 *            待加密内容
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String strKey, String strIn) throws Exception {
		// 获取加密密钥
		SecretKeySpec skeySpec = getKey(strKey);
		// 获取加密密码模式
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0506070801020304".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(strIn.getBytes());
		return parseByte2HexStr(encrypted);
	}

	/**
	 * 解密
	 * 
	 * @param strKey
	 *            加密key
	 * @param strIn
	 *            待解密内容
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String strKey, String strIn) throws Exception {
		SecretKeySpec skeySpec = getKey(strKey);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0506070801020304".getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] encrypted1 = parseHexStr2Byte(strIn);

		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original);
		return originalString;
	}

	/**
	 * 通过加密key制作密钥
	 * 
	 * @param strKey
	 *            加密key
	 * @return
	 * @throws Exception
	 */
	private static SecretKeySpec getKey(String strKey) throws Exception {
		if (strKey == null || "".equals(strKey)) {
			strKey = DEFALUT_KEY;
		}
		byte[] arrBTmp = strKey.getBytes();
		byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");
		return skeySpec;
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * 
	 * @return
	 */

	public static byte[] parseHexStr2Byte(String hexStr) {

		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}

		return result;

	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * 
	 * @return
	 */

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}


	private static byte[] readInputStream(InputStream inStream) throws IOException {
		byte[] buffer = new byte[1024];
		byte[] result = null;
		int len = 0;
		try (ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			result = outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inStream.close();
			buffer = null;
		}
		return result;
	}

	private static String testAPI(String text) {
		String template = "{\"originatePartyId\":\"0001\",\"originateTellerId\":\"xxx\",\"channel\":\"abc1ff706c4b43ac20305ab74b5e181a\",\"channelDate\":\"xxx\",\"channelSerialNo\":\"xxx\",\"wsno\":\"xxx\",\"domain\":\"csbot\",\"text\":\"%s\",\"appId\":\"f6d9ffee783f88927c80a803939fa874\",\"userId\":\"123\",\"custId\":\"xxx\",\"sessionId\":\"xxx\",\"clientId\":\"xxx\",\"sourceId\":\"0\",\"uuid\":\"xxx-xxxx-xxxxxx-xxx\",\"extendData\":{\"serviceType\":\"0\"}}";
		String u = "http://chat-api.emotibot.com:8080/openapicmbc";

		String content = "";

		HttpURLConnection conn = null;
		try {
			// get query post data
			String postData = String.format(template, text);
			URL url = new URL(u);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(2000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			conn.setRequestProperty("accept-charset", "UTF-8");
			// post request data
			OutputStream os = conn.getOutputStream();
			os.write(postData.getBytes(StandardCharsets.UTF_8));
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			} else {
				InputStream stream = conn.getInputStream();
				byte[] data = readInputStream(stream);
				content = new String(data, StandardCharsets.UTF_8);

			}

		} catch (Exception e) {
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return content;
	}



	public static void main(String[] arg0) throws Exception {
//		String httpPostWithJson = "{" + "\"channelDate\":\"点点滴滴\"," + "\"channelSerialNo\": \"xxx\","
//				+ "\"domain\": \"csbot\"," + "\"info\": {" + "\"intent\": \"\"," + "\"emotion\": \"中性\","
//				+ "\"module\": \"faq\"," + "\"score\": 79.87 " + "}," + "\"type\": \"S\"," + "\"status\": 0,"
//				+ "\"message\": \"success\"," + "\"tspan\": 157," + "\"data\": [" + "{" + "\"type\": \"text\","
//				+ "\"subType\": \"guslist\"," + "\"value\": \"我不太确定你的问题，你要问的问题是不是以下：\"," + "\"data\": ["
//				+ "\"个人网银私人银行功能介绍|个人网银私人银行功能介绍\"," + "\"个人网银查询金批量转账明细|个人网银私人银行功能介绍\","
//				+ "\"个人网银信用卡购汇方式删除|个人网银私人银行功能介绍\"," + "\"个人网银查询民生金批量转账明细|个人网银私人银行功能介绍\","
//				+ "\"个人网银基金委托查询|个人网银私人银行功能介绍\"" + "]," + "\"extendData\": \"2到底\"" + "}" + "],"
//				+ "\"systemInfo\": \"热热\"," + "\"extendData\": \"阿瑟斯岁\"" + "}";
//		System.out.println("========未加密内容==========" + httpPostWithJson);
//		String encryptKey = AESUtil.encrypt("eqwqrtfq5er", httpPostWithJson);
//		System.out.println("========加密内容==========" + encryptKey);
//		String decrypt = AESUtil.decrypt("eqwqrtfq5er", encryptKey);
//		System.out.println("========解密内容==========" + decrypt);


		String encryptKey = testAPI("1");
		System.out.println("========加密内容==========" + encryptKey);
		String decrypt = AESUtil.decrypt("eqwqrtfq5er", encryptKey);
		System.out.println("========解密内容==========" + decrypt);

	}
}
