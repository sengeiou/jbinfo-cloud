package cn.jbinfo.api.encrypt;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * <p>
 * SHA1 签名算法
 * </p>
 *
 * @Date 2015-01-09
 */
public class SHA1 {

    /**
     * <p>
     * 用SHA1算法生成安全签名
     * </p>
     *
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     * @throws AESException {@link AESException}
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AESException {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            StringBuffer sb = new StringBuffer();

			/* 字符串排序 */
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }

			/* SHA1签名生成 */
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sb.toString().getBytes());
            byte[] digest = md.digest();

            StringBuilder hexstr = new StringBuilder();
            String shaHex;
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }

            return hexstr.toString();
        } catch (Exception e) {
            throw new AESException(AESException.ERROR_COMPUTE_SIGNATURE, e);
        }
    }

    /**
     * <p>
     * 用SHA1算法生成安全签名
     * </p>
     *
     * @param bucket    系统标识
     * @param timestamp 时间戳
     * @param userName  用户名
     * @param password  密码
     * @param deviseId  设备标识
     * @param versionId 版本号
     * @param encrypt   密文
     * @return 安全签名
     * @throws AESException {@link AESException}
     */
    public static String getSHA1(String bucket, String timestamp, String userName, String password,
                                 String devise, String deviseId, String versionId,
                                 String encrypt) throws AESException {
        try {
            String[] array = new String[]{bucket, timestamp, userName, password, devise, deviseId, versionId, encrypt};
            StringBuffer sb = new StringBuffer();

			/* 字符串排序 */
            Arrays.sort(array);
            for (int i = 0; i < 8; i++) {
                sb.append(array[i]);
            }

			/* SHA1签名生成 */
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sb.toString().getBytes());
            byte[] digest = md.digest();

            StringBuilder hexstr = new StringBuilder();
            String shaHex;
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            throw new AESException(AESException.ERROR_COMPUTE_SIGNATURE, e);
        }
    }

    /**
     *用SHA1算法生成安全签名
     * @param bucket
     * @param timestamp
     * @param unionId
     * @param device
     * @param deviceId
     * @param versionId
     * @param secretKey
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSHA1(String bucket, String timestamp, String unionId, String device, String deviceId, String versionId, String secretKey) throws NoSuchAlgorithmException {

            String[] array = new String[]{bucket, timestamp, unionId, device, deviceId, versionId, secretKey};
            StringBuffer sb = new StringBuffer();

			/* 字符串排序 */
            Arrays.sort(array);
            for (int i = 0; i < 7; i++) {
                sb.append(array[i]);
            }

			/* SHA1签名生成 */
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sb.toString().getBytes());
            byte[] digest = md.digest();

            StringBuilder hexstr = new StringBuilder();
            String shaHex;
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
    }
}