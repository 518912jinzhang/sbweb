package com.sbweb.common;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractService {

	/**
	 * @param byteArrLen 返回数组字节长度
	 * @param startSub 源字节数组开始截取位置
	 * @param sourceByte 源字节数组
	 * 
	 * @return 目标字节数组
	 * */
	public byte[] subBytes(int byteArrLen, 
			AtomicInteger dataSubStart, byte[] sourceByte) {
		byte[] byteArr = new byte[byteArrLen];
		System.arraycopy(sourceByte, dataSubStart.intValue(), byteArr, 0, byteArrLen);
		
		dataSubStart.getAndAdd(byteArrLen);
		return byteArr;
	}

	/**
	 * @param byteArrLen 返回数组字节长度
	 * @param startSub 源字节数组开始截取位置
	 * @param sourceByte 源字节数组
	 * 
	 * @return 目标字节数组
	 * */
	public int subBytes2Int(int byteArrLen, 
			AtomicInteger dataSubStart, byte[] sourceByte) {
		byte[] bytes = subBytes(byteArrLen,dataSubStart,sourceByte);
		return byteArrayToInt(bytes);
	}
	
	/**
	 * @param byteArrLen 返回数组字节长度
	 * @param startSub 源字节数组开始截取位置
	 * @param sourceByte 源字节数组
	 * 
	 * @return 目标字节数组
	 * */
	public String subBytes2String(int byteArrLen, 
			AtomicInteger dataSubStart, byte[] sourceByte) {
		byte[] bytes = subBytes(byteArrLen,dataSubStart,sourceByte);
		
		String str = "";
		try {
			str = new String(bytes, Constants.SOCKET_ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * @param byteArrLen 返回数组字节长度
	 * @param startSub 源字节数组开始截取位置
	 * @param sourceByte 源字节数组
	 * 
	 * @return 目标字节数组
	 * */
	public String subBytes2Substring(int byteArrLen, 
			AtomicInteger dataSubStart, byte[] sourceByte) {
		byte[] bytes = subBytes(byteArrLen,dataSubStart,sourceByte);
		String prop = "";
		try {
			prop = new String(bytes, Constants.SOCKET_ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		prop = prop.substring(2);
		return prop;
	}
	
	/**
     * 字节数组转int,适合转高位在前低位在后的byte[]
     * 
     * @param bytes
     * @return
     */
    public int byteArrayToInt(byte[] bytes) {
        int result = 0;
        int len = bytes.length;
        if (len == 1) {
            byte ch = (byte) (bytes[0] & 0xff);
            result = ch;
        } else if (len == 2) {
            int ch1 = bytes[0] & 0xff;
            int ch2 = bytes[1] & 0xff;
            result = (short) ((ch1 << 8) | (ch2 << 0));
        } else if (len == 4) {
            result = bytes[3] & 0xFF | (bytes[2] & 0xFF) << 8 | (bytes[1] & 0xFF) << 16 | (bytes[0] & 0xFF) << 24;
        } else if (len == 8) {
            long ch1 = bytes[0] & 0xff;
            long ch2 = bytes[1] & 0xff;
            long ch3 = bytes[2] & 0xff;
            long ch4 = bytes[3] & 0xff;
            long ch5 = bytes[4] & 0xff;
            long ch6 = bytes[5] & 0xff;
            long ch7 = bytes[6] & 0xff;
            long ch8 = bytes[7] & 0xff;
            result = (int) ((ch1 << 56) | (ch2 << 48) | (ch3 << 40) | (ch4 << 32) | (ch5 << 24) | (ch6 << 16) | (ch7 << 8) | (ch8 << 0));
        }
        return result;
    }
    
	public byte[] int2byte(int res) { 
		byte[] targets = new byte[4]; 

		targets[3] = (byte) (res & 0xff);// ���λ 
		targets[2] = (byte) ((res >> 8) & 0xff);// �ε�λ 
		targets[1] = (byte) ((res >> 16) & 0xff);// �θ�λ 
		targets[0] = (byte) (res >>> 24);// ���λ,�޷������ơ� 
		return targets; 
	}
	
	public byte[] longToByteArray(long res) {
		byte[] targets = new byte[4]; 

		targets[3] = (byte) (res & 0xff);// ���λ 
		targets[2] = (byte) ((res >> 8) & 0xff);// �ε�λ 
		targets[1] = (byte) ((res >> 16) & 0xff);// �θ�λ 
		targets[0] = (byte) (res >>> 24);// ���λ,�޷������ơ� 
		return targets; 
	}
	
	public byte[] strToByteArray(String str) {
	    if (str == null) {
	        return null;
	    }
	    byte[] byteArray = null;
		try {
			byteArray = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return byteArray;
	}
}
