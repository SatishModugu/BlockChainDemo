package transferApp;

import java.security.PublicKey;

import basic.hashing;

public class OutputTransaction {
	public String TransactionId;
	public PublicKey recievier; //also known as the new owner of these coins.
	public float amount; //the amount of coins they own
	public String parentID; //the id of the transaction this output was created in
	
	//Constructor
	public OutputTransaction(PublicKey recievier, float amount, String parentID) {
		this.recievier = recievier;
		this.amount = amount;
		this.parentID = parentID;
		this.TransactionId = hashing.hashSha256(hashing.KeyToString(recievier)+Float.toString(amount)+parentID);
	}
	
	//Check if coin belongs to you
	public boolean isValid(PublicKey keyPublic) {
		return (keyPublic == recievier);
	}
	
}