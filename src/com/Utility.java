package com;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Utility {
	public static String md5(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}
	 public static String decodeUnicode(String str) {
		  Charset set = Charset.forName("UTF-16");
		  Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
		  Matcher m = p.matcher( str );
		  int start = 0 ;
		  int start2 = 0 ;
		  StringBuffer sb = new StringBuffer();
		  while( m.find( start ) ) {
		   start2 = m.start() ;
		   if( start2 > start ){
		    String seg = str.substring(start, start2) ;
		    sb.append( seg );
		   }
		   String code = m.group( 1 );
		   int i = Integer.valueOf( code , 16 );
		   byte[] bb = new byte[ 4 ] ;
		   bb[ 0 ] = (byte) ((i >> 8) & 0xFF );
		   bb[ 1 ] = (byte) ( i & 0xFF ) ;
		   ByteBuffer b = ByteBuffer.wrap(bb);
		   sb.append( String.valueOf( set.decode(b) ).trim() );
		   start = m.end() ;
		  }
		  start2 = str.length() ;
		  if( start2 > start ){
		   String seg = str.substring(start, start2) ;
		   sb.append( seg );
		  }
		  return sb.toString() ;
		 }
}
