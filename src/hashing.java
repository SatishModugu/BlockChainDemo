import java.security.MessageDigest;
import com.google.gson.GsonBuilder;
public class hashing {
	
		public static String hashSha256(String inputdata){		
			try {
				MessageDigest hasher = MessageDigest.getInstance("SHA-256");	        
				byte[] hashSHA256 = hasher.digest(inputdata.getBytes("UTF-8"));	        
				StringBuffer hexdecimalHash = new StringBuffer();
				for (int i = 0; i < hashSHA256.length; i++) {
					String hexdeci = Integer.toHexString(0xff & hashSHA256[i]);
					if(hexdeci.length() == 1) hexdecimalHash.append('0');
					hexdecimalHash.append(hexdeci);
				}
				return hexdecimalHash.toString();
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		//Converting into JSON format
		public static String JsonBuilder(Object obj) {
			return new GsonBuilder().setPrettyPrinting().create().toJson(obj);
		}
		
		//This function returns proof of work difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"  
		public static String proofOfWorkString(int proofInt) {
			return new String(new char[proofInt]).replace('\0', '0');
		}
		
	}
