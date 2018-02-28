import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConvertResultsetToObject {

	//Gönderilen class türünde nesne olarak  döndürür.Resultsetin her döngüsünde çağrılmalıdır.
	public static <T> Object getObject(ResultSet rs, @SuppressWarnings("rawtypes") Class pObj)
			throws SQLException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
		Field[] fields = pObj.getDeclaredFields();

		@SuppressWarnings("unchecked")
		T row = (T) pObj.newInstance();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals("tableName"))
				continue;
			row.getClass().getDeclaredMethod("set" + field.getName(), field.getType()).invoke(row,
					rs.getObject(field.getName()));
		}
		return row;
	}
	//Liste Olarak döndürür Resultseti while döngüsüne sokmaya gerek yoktur.
public static <T> Object getObjectList(ResultSet rs, @SuppressWarnings("rawtypes") Class pObj)
			throws SQLException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
		Field[] fields = pObj.getDeclaredFields();
		java.util.List<T> result = new ArrayList<>();
		while (rs.next()) {
			@SuppressWarnings("unchecked")
			T row = (T) pObj.newInstance();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.getName().equals("tableName"))
					continue;
				row.getClass().getDeclaredMethod("set" + field.getName(), field.getType()).invoke(row,
						rs.getObject(field.getName()));

			}
			result.add(row);
		}
		return result;
	}

}
