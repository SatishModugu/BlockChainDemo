package basic;
import java.security.MessageDigest;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import com.google.gson.GsonBuilder;

import transferApp.TransferHandler;
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
		
		public static String KeyToString(Key key) {
			return Base64.getEncoder().encodeToString(key.getEncoded());
		}
		//Applies ECDSA Signature and returns the result ( as bytes ).
				public static byte[] ECDSASigGenerator(PrivateKey KeyPriv, String inp) {
				Signature ecdsa;
				byte[] result = new byte[0];
				try {
					ecdsa = Signature.getInstance("ECDSA", "BC");
					ecdsa.initSign(KeyPriv);
					byte[] strByte = inp.getBytes();
					ecdsa.update(strByte);
					byte[] finalSig = ecdsa.sign();
					result = finalSig;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return result;
			}
				
				//Verifies a String signature 
				public static boolean SigVerifyier(PublicKey KeyPub, String input, byte[] ecdsasig) {
					try {
						Signature verify = Signature.getInstance("ECDSA", "BC");
						verify.initVerify(KeyPub);
						verify.update(input.getBytes());
						return verify.verify(ecdsasig);
					}catch(Exception e) {
						throw new RuntimeException(e);
					}
				}
				
				//Tacks in array of transactions and returns a merkle root.
				public static String merkleRootGenerator(ArrayList<TransferHandler> transfers) {
						int c = transfers.size();
						ArrayList<String> layerOfPrivTree = new ArrayList<String>();
						for(TransferHandler transaction : transfers) {
							layerOfPrivTree.add(transaction.txId);
						}
						ArrayList<String> newLayer = layerOfPrivTree;
						while(c > 1) {
							newLayer = new ArrayList<String>();
							for(int i=1; i < layerOfPrivTree.size(); i++) {
								newLayer.add(hashSha256(layerOfPrivTree.get(i-1) + layerOfPrivTree.get(i)));
							}
							c = newLayer.size();
							layerOfPrivTree = newLayer;
						}
						String newMerkleRoot = (newLayer.size() == 1) ? newLayer.get(0) : "";
						return newMerkleRoot;
					}
		
	}
