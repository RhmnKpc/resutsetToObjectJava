import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConvertResultsetToObject {
	
	public static <T> Object convertRsToObject(Class pClass, ResultSet pResultSet)
			throws SQLException, SecurityException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		T W_MyClass = null;

		Object result2 = null;
		Method[] methods = pClass.newInstance().getClass().getDeclaredMethods();
		java.util.List<Method> AllMetods = new ArrayList();
		for (Method metod : methods) {
			AllMetods.add(metod);
		}
		if (pClass.getSuperclass() != null) {
			Method[] methodsSuper = pClass.getSuperclass().getDeclaredMethods();
			for (Method item : methodsSuper)
				AllMetods.add(item);
		}
		int i = 0;
		java.sql.ResultSetMetaData rsmd = pResultSet.getMetaData();
		int W_ColumnCount = rsmd.getColumnCount();
		// while (pResultSet.next()) {
		W_MyClass = (T) pClass.newInstance();
		for (i = 0; i < W_ColumnCount; i++) {
			for (int l = 0; l < AllMetods.size(); l++) {
				if (rsmd.getColumnName(i + 1).toLowerCase().equalsIgnoreCase(
						AllMetods.get(l).getName().substring(3, AllMetods.get(l).getName().length()).toLowerCase())
						&& AllMetods.get(l).getName().substring(0, 3).equalsIgnoreCase("set")) {
					Object setObject = pResultSet.getObject(i + 1);
					Method method = pClass.newInstance().getClass().getMethod(AllMetods.get(l).getName(),
							AllMetods.get(l).getParameterTypes()[0]);
					method.setAccessible(true);
					switch (AllMetods.get(l).getParameterTypes()[0].toString()) {
					case "int":
						setObject = Integer.valueOf(setObject.toString());
						break;
					case "double":
						setObject = Double.valueOf(setObject.toString());
						break;
					case "short":
						setObject = Short.valueOf(setObject.toString());
						break;
					default:
						setObject = AllMetods.get(l).getParameterTypes()[0].cast(setObject);
						break;
					}
					method.invoke(W_MyClass, setObject);
				}
			}
		}
		if (i > 0) {
			result2 = W_MyClass;
		}
		return result2;
	}
}
