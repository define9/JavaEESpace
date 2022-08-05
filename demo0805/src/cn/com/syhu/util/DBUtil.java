package cn.com.syhu.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	// 静态常量
	public static final String URL = "jdbc:mysql://127.0.0.1:3306/staff?serverTimezone=GMT%2b8&useSSL=false&useUnicode=true&characterEncoding=utf-8";
	public static final String USER = "root";
	public static final String PWD = "root";
	
	// 获取连接的方法
	private Connection getConn() throws ClassNotFoundException, SQLException {
		// 1. 加载驱动
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		// 2. 创建连接对象 
		return DriverManager.getConnection(URL, USER, PWD);
	}
	
	// 关闭资源
	private void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			if (stmt != null) stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 提取一个方法，可以同时解决CUD的操作
	public int update(String sql, Object... params) {
		// 声明变量
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// 2. 创建连接对象
			conn = getConn();
			
			// 3. 执行对象 
			stmt = conn.prepareStatement(sql);
			
			// 绑定参数
			if (params != null) {
				for (int i=0; i<params.length; i++) {
					stmt.setObject(i+1, params[i]);
				}
			}
			
			// 4. 执行SQL
			int result = stmt.executeUpdate();
			
			// 5. 处理返回结果
			return result;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, null);
		}
		
		return 0;
	}
	
	// 查询一条， 返回map
	public Map<String, Object> selectOne(String sql, Object...params){
		List<Map<String, Object>> list = select(sql, params);
		
		if (list != null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	// 查询一条，返回实体类
	public <T> T selectOne(Class<T> cls, String sql, Object...params) {
		Map<String, Object> map = selectOne(sql, params);
		
		if (map != null) {
			return mapToObject(map, cls);
		} else {
			return null;
		}
	}
	
	// 能将查询结果中的每一条记录，从Map->指定类型的对象 
	public <T> List<T> select(Class<T> cls,String sql, Object... params){
		
		// 调用selec获取一个Map
		List<Map<String, Object>> list = select(sql, params);
		List<T> res = new ArrayList();
		// List<Staff>  res
		
		if (list != null) {
			for (Map<String, Object> map : list) {
				T t = mapToObject(map, cls);
				
				res.add(t);
			}
			
			return res;			
		}
		
		return null;
	}
	
	// 将一个map对象，转换成指定类型的对象。
	private <T> T mapToObject(Map<String, Object> map, Class<T> cls) {
		// 反射+内省，内省是对反射的封装，基于JavaBean
		try {
			// 反射创建对象
			T t = cls.newInstance(); // t = new Dept();
			// 获取cls类的JavaBean信息
			BeanInfo beanInfo = Introspector.getBeanInfo(cls); // 获取Dept的信息 主要是属性，属性的Get和Set
			// 获取cls类的属性信息
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors(); // 获取所有属性的描述
			
			// 遍历所有的属性
			for (PropertyDescriptor prop : props) {  // id, name, room, phone
				// 属性的名字
				String propName = prop.getName();
				Method setter = prop.getWriteMethod();	// set方法  setId
				//prop.getReadMethod();	// get方法
				
				// 调用set方法，将map中的值设置到属性上
				Object value = map.get(propName);
				if (value != null && setter != null) {
					try {
						setter.invoke(t, value);  // 执行Method, 调用t.setId(value)
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			
			return t;  // 返回t对象
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// 通用的返回Map类型的方法
	public List<Map<String, Object>> select(String sql, Object... params){
		// 声明变量
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		// 声明集合
		List<Map<String, Object>> list = new ArrayList();
		
		try {
			// 2. 创建连接对象 
			conn = getConn();
			
			// 3. 执行对象 
			stmt = conn.prepareStatement(sql);
			
			// 绑定参数
			if (params != null) {
				for (int i=0; i<params.length; i++) {
					stmt.setObject(i+1, params[i]);
				}
			}
			
			// 4. 执行SQL
			rs = stmt.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();		// Meta元  MetaSpace
			//md.getColumnCount();	// 获取查询结果集的列的数量
			//md.getColumnName(1);	// 根据列的下标，获取列名
			//md.getColumnLabel(1); 	// 根据列的下标，获取别名
			
			// 5. 处理返回结果
			while (rs.next()) {
				Map<String, Object> row = new HashMap();
				
				// 如果读取查询结果中的数据
				for (int i=1; i<=md.getColumnCount(); i++) {
					String colName = md.getColumnLabel(i);
					Object value = rs.getObject(i);
					
					row.put(colName, value);
				}
				
				list.add(row);
			}
			
			return list;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, stmt, rs);
		}
		
		return null;
	}

}
