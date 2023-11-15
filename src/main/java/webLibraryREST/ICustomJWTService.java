package webLibraryREST;

import com.prog2.labs.db.entity.User;

public interface ICustomJWTService {

	 String GetToken(User user);
}
