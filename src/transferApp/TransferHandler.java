package transferApp;


import java.security.*;
import java.util.ArrayList;
import basic.hashing;

public class TransferHandler {
	
	public String txId; // this is also the hash of the transaction.
	public PublicKey sendersKey; // senders address/public key.
	public PublicKey reciversKey; // Recipients address/public key.
	public float amount;
	public byte[] uniqueSig; // this is to prevent anybody else from spending funds in our wallet.
	
	public ArrayList<InputTransaction> TxInput = new ArrayList<InputTransaction>();
	public ArrayList<OutputTransaction> TxOutput = new ArrayList<OutputTransaction>();
	
	private static int txNumber = 0; 
	
	// Constructor: 
	public TransferHandler(PublicKey from, PublicKey to, float value,  ArrayList<InputTransaction> inputs) {
		this.sendersKey = from;
		this.reciversKey = to;
		this.amount = value;
		this.TxInput = inputs;
	}
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String applyHash() {
		txNumber++; 
		return hashing.hashSha256(
				hashing.KeyToString(sendersKey) +
				hashing.KeyToString(reciversKey) +
				Float.toString(amount) + txNumber
				);
	}
	//Signs all the data we don't wish to be tampered with.
	public void createSig(PrivateKey Keypri) {
		String data = hashing.KeyToString(sendersKey) + hashing.KeyToString(reciversKey) + Float.toString(amount);
		uniqueSig = hashing.ECDSASigGenerator(Keypri,data);	
	}
	//Verifies the data we signed hasn't been tampered with
	public boolean validateSig() {
		String data = hashing.KeyToString(sendersKey) + hashing.KeyToString(reciversKey) + Float.toString(amount)	;
		return hashing.SigVerifyier(sendersKey, data, uniqueSig);
	}
	//Returns true if new transaction could be created.	
	public boolean processTransaction() {
			
			if(validateSig() == false) {
				System.out.println("#Transaction Signature failed to verify");
				return false;
			}
					
			//gather transaction inputs (Make sure they are unspent):
			for(InputTransaction i : TxInput) {
				i.Unspent = MainApp.UnspentTxs.get(i.transactionIdOp);
			}

			//check if transaction is valid:
			if(getInputAmount() < MainApp.minimumTxAmount) {
				System.out.println("#Transaction Inputs to small: " + getInputAmount());
				return false;
			}
			
			//generate transaction outputs:
			float leftOver = getInputAmount() - amount; //get value of inputs then the left over change:
			txId = applyHash();
			TxOutput.add(new OutputTransaction( this.reciversKey, amount,txId)); //send value to recipient
			TxOutput.add(new OutputTransaction( this.sendersKey, leftOver,txId)); //send the left over 'change' back to sender		
					
			//add outputs to Unspent list
			for(OutputTransaction oT : TxOutput) {
				MainApp.UnspentTxs.put(oT.TransactionId , oT);
			}
			
			//remove transaction inputs from UTXO lists as spent:
			for(InputTransaction iT : TxInput) {
				if(iT.Unspent == null) continue; //if Transaction can't be found skip it 
				MainApp.UnspentTxs.remove(iT.Unspent.TransactionId);
			}
			
			return true;
		}
		
	//returns sum of inputs(UTXOs) values
		public float getInputAmount() {
			float total = 0;
			for(InputTransaction i : TxInput) {
				if(i.Unspent == null)
					continue; //if Transaction can't be found skip it 
				total += i.Unspent.amount;
			}
			return total;
		}

	//returns sum of outputs:
		public float getOutputAmount() {
			float totalAmount = 0;
			for(OutputTransaction o : TxOutput) {
				totalAmount += o.amount;
			}
			return totalAmount;
		}
}