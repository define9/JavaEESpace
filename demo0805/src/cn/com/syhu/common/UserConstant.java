package cn.com.syhu.common;

public class UserConstant {
	public enum StatusEnum {
		
		AVAILABLE(0, "可用"), DISABLE(1, "禁用");
		
		StatusEnum(int c, String m){
			code = c;
			msg = m;
		}
		
		private int code;
		private String msg;
		
		public int getCode() {
			return code;
		}
		public String getMsg() {
			return msg;
		}
		
		
	}
}
