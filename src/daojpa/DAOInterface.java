package daojpa;

import java.util.List;

public interface DAOInterface<T> {
	 void create(T obj);
	 T read(Object chave);
	 T update(T obj);
	 void delete(T obj) ;
	 List<T> readAll();
}
