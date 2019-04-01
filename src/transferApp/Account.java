package transferApp;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Account {
	
	public PrivateKey keyPri;
	public PublicKey keyPub;
	public HashMap<String,OutputTransaction> UnspentTxs = new HashMap<String,OutputTransaction>();
	
	public Account(){
		keyGenerator();	
	}
		
	public void keyGenerator() {
		try {
			KeyPairGenerator keys = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom randomNumber = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			keys.initialize(ecSpec, randomNumber);   //256 bytes 
	        	KeyPair PubandPriv = keys.generateKeyPair();
	        	keyPri = PubandPriv.getPrivate();
	        	keyPub = PubandPriv.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public float accountBal() {
		float totalBalance = 0;	
        for (Map.Entry<String, OutputTransaction> item: MainApp.Unspent.entrySet()){
        	OutputTransaction unspent = item.getValue();
            if(unspent.isValid(keyPub)) { //if output belongs to me ( if coins belong to me )
            	UnspentTxs.put(unspent.TransactionId,unspent); //add it to our list of unspent transactions.
            	totalBalance += unspent.amount ; 
            }
        }  
		return totalBalance;
	}
	
	public TransferHandler sendAmount(PublicKey rxPubKey, float amount ) {
		if(accountBal() < amount) {
			System.out.println("No enough funds to send. Transaction Denined.");
			return null;
		}
		ArrayList<InputTransaction> inTx = new ArrayList<InputTransaction>();
		
		float total = 0;
		for (Map.Entry<String, OutputTransaction> item: UnspentTxs.entrySet()){
			OutputTransaction unspent = item.getValue();
			total += unspent.amount ;
			inTx.add(new InputTransaction(unspent.TransactionId));
			if(total > amount) break;
		}
		
		TransferHandler txNew = new TransferHandler(keyPub, rxPubKey , amount, inTx);
		txNew.createSig(keyPri);
		
		for(InputTransaction inp: inTx){
			UnspentTxs.remove(inp.transactionIdOp);
		}
		
		return txNew;
	}
}