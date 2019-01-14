package sms.imobpay.com.testsmslinkcore;

import java.net.URLEncoder;

/**
 * 类说明： 字符串操作类
 * 
 * @author @Cundong
 * @weibo http://weibo.com/liucundong
 * @blog http://www.liucundong.com
 * @date Apr 29, 2011 2:50:48 PM
 * @version 1.0
 */
public class StringUtils {
	private static final int DEF_DATA_SIZE = 12; //为啥是12，没明白
	/**
	 * 判断给定字符串是否空白串。<br>
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isBlank(String input) {
		if (null == input || "".equals(input.trim()))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查输入数据是否为空，或者空指针
	 * @param input
	 * @return: 不为空，返回true;否则返回false
	 */
	public static boolean isNotEmptyOrNull(String input){
		boolean ret = true;
		if (null == input || "".equals(input.trim()))
		{
			ret = false;
		}
		return ret;
	}

	/**
	 * byte转16进制
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		String result = "";
		try {
			result = URLEncoder.encode(Base64Utils.encode(src), "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/* *
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 *
	 * @param src byte[] data
	 *
	 * @return hex string
	 */
	public static String changeBytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 判断一个String字段是不是空值；
	 */
	public static String handleNullString(String check_str) {
		if (null != check_str) {
			return check_str;
		}
		return "";
	}

	/**
	 * 判断一个字符串是否全为数字
	 *
	 * @param numStr
	 * @return
	 */
	public static boolean isNumberStr(String numStr) {
		boolean ret = false;
		if (null != numStr && !"".equals(numStr.trim())) {
			try {
				Double.valueOf(numStr);//把字符串强制转换为数字
				ret = true;//如果是数字，返回True
			} catch (Exception e) {
				ret = false;//如果抛出异常，返回False
			}
		}
		return ret;
	}

	/**
	 * 分段显示，string需要分段显示的，mark 是否需要前段显示的特殊符号
	 */
	public static String subStringToShow(String string, String mark) {
		String show_str = "";
		if (null != string) {
			if ("-".equals(string.substring(0, 1))) {
				if (string.length() > 3) {
					show_str = string.substring(0, string.length() - 2) + "." + string.substring(string.length() - 2);
				} else if (string.length() == 3) {
					show_str = "-0." + string.substring(1);
				} else if (string.length() == 2) {
					show_str = "-0.0" + string.substring(1);
				} else {
					show_str = "0.00";
				}
			} else {
				if (string.length() > 2) {
					show_str = mark + string.substring(0, string.length() - 2) + "." + string.substring(string.length() - 2);
				} else if (string.length() == 2) {
					show_str = mark + "0." + string;
				} else {
					show_str = mark + "0.0" + string;
				}
			}
		} else {
			show_str = "0.00";
		}
		return show_str;
	}

	//返回的时间，处理后显示
	public static String timeToShow(String dataTime) {
		String showtime = null;
		if (null != dataTime) {
			int size = dataTime.length();
			switch (size) {
				case 8:
					showtime = dataTime.substring(0, 4) + "-" + dataTime.substring(4, 6) + "-" + dataTime.substring(6);
					break;
				case 14:
					showtime = dataTime.substring(0, 4) + "-" + dataTime.substring(4, 6) + "-" + dataTime.substring(6, 8)
							+ " " + dataTime.substring(8, 10) + ":" + dataTime.substring(10, 12)
							+ ":" + dataTime.substring(12);
					break;
				default:
					break;
			}
		} else {
			showtime = "";
		}
		return showtime;
	}

	//返回时间以xx年xx月显示
	public static String yearAndMonth(String dataTime) {
		String show_str = null;
		if (null != dataTime && dataTime.length() > 5) {
			show_str = dataTime.substring(0, 4) + "年" + Integer.parseInt(dataTime.substring(4, 6)) + "月";
		} else {
			show_str = "";
		}
		return show_str;
	}

	/**
	 *
	 * @说明：判断是否为负数
	 * @Parameters 无
	 * @return 无
	 */
	public static String checkMoney(String money) {
		String data = "";
		if (null != money && money.length() > 0) {
			if ("-".equals(money.substring(0, 1)) || "0.00".equals(money)) {
				data = "0.00";
			} else {
				data = money;
			}
		}
		return data;
	}

	/**
	 * 按照固定格式获取左边数据
	 * @param value
	 * @return
	 */
	public static String getLeftValue(String value) {
		String[] values;
		if (value != null) {
			values = value.split("\\|");
			return values[0];
		}
		return "";
	}

	/**
	 * 按照固定格式获取右边数据
	 * @param value
	 * @return
	 */
	public static String getRightValue(String value) {
		String[] values;
		if (value != null) {
			values = value.split("\\|");
			if (values.length == 2) {
				return values[1];
			}
			return "";
		}
		return "";
	}


	/**
	 * 检测传入的是否是color
	 *
	 * @return
	 */
	public static boolean checkColor(String color) {
		boolean iscolor = false;
		if (isNotEmptyOrNull(color)) {
			if (color.indexOf("#") == 0) {
				iscolor = true;
			}
		}
		return iscolor;
	}

	/**
	 * 返回数字取绝对值
	 *
	 * @return
	 */
	public static int getDataSize(String data_size) {
		int size = 0;
		if (isNotEmptyOrNull(data_size)) {
			if (data_size.indexOf("-") == 0) {
				if (data_size.length() > 1) {
					if (isNumberStr(data_size)) {
						size = Integer.parseInt(data_size);
					}
				} else {
					size = DEF_DATA_SIZE;
				}
			} else {
				if (isNumberStr(data_size)) {
					size = Integer.parseInt(data_size);
				}
			}
		}
		return size;
	}







}