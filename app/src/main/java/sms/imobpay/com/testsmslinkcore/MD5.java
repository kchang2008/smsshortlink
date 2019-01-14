package sms.imobpay.com.testsmslinkcore;

/**
 * Created by jun on 17/9/19.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static void main(String arg[]) {
        String myinfo = "我的测试信息";
        System.out.println(myinfo);
        System.out.println(md5Encode(myinfo));
    }

    public static String md5Encode(String input) {
        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(input.getBytes());
            byte[] digesta = alg.digest();
            return byte2Hex(digesta);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //转换下
    static String byte2Hex(byte[] paramArrayOfByte) {
        StringBuffer localStringBuffer = new StringBuffer();
        String str = "";
        for (int i = 0; i < paramArrayOfByte.length; ++i) {
            str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
            if (str.length() == 1)
                localStringBuffer.append("0");
            localStringBuffer.append(str);
        }
        return localStringBuffer.toString().toUpperCase();
    }
}