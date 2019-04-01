package transferApp;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import basic.basicblock;
import basic.hashing;

public class MainApp {

	public static ArrayList<basicblock> chainof = new ArrayList<basicblock>();
	public static HashMap<String,OutputTransaction> Unspent = new HashMap<String,OutputTransaction>(); //list of all unspent transactions.
	public static int difficulty = 5;
	public static float minimumTxAmount = 0.1f;
	public static Account AccountA;
	public static Account AccountB;

	public static void main(String[] args) {	
		//Setup Bouncey castle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
		//Create the new wallets
		AccountA = new Account();
		AccountB = new Account();
		//Test public and private keys
		System.out.println("Private and public keys:");
		System.out.println(hashing.KeyToString(AccountA.keyPri));
		System.out.println(hashing.KeyToString(AccountA.keyPub));
		//Create a test transaction from WalletA to walletB 
		TransferHandler transaction = new TransferHandler(AccountA.keyPub, AccountB.keyPub, 5, null);
		transaction.createSig(AccountA.keyPri);
		//Verify the signature works and verify it from the public key
		System.out.println("Is signature verified");
		System.out.println(transaction.validateSig());	
	}
}