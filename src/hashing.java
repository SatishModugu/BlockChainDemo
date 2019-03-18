import java.security.MessageDigest;
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
	}
