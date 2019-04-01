package transferApp;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import com.google.gson.GsonBuilder;
import basic.basicblock;
import basic.hashing;
public class MainApp {
	public static ArrayList<basicblock> chainOfBlocks = new ArrayList<basicblock>();
	public static HashMap<String,OutputTransaction> UnspentTxs = new HashMap<String,OutputTransaction>(); //list of all unspent transactions.
	public static int diffPoW = 5;
	public static float minimumTxAmount = 0.1f;
	public static Account AccountA;
	public static Account AccountB;
	public static TransferHandler firstTx;
	public static void main(String[] args) {	
		//Setup Bouncey castle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
		//Create the new wallets
		AccountA = new Account();
		AccountB = new Account();
		Account amount = new Account();
		//create first transaction that sends an amount of 100 to Account A: 
		firstTx = new TransferHandler(amount.keyPub, AccountA.keyPub, 100f, null);
		firstTx.createSig(amount.keyPri);	 //signature manually to the first transaction	
		firstTx.txId = "0"; //manually set the transaction id
		firstTx.TxOutput.add(new OutputTransaction(firstTx.reciversKey, firstTx.amount, firstTx.txId)); //manually add the Transactions Output
		UnspentTxs.put(firstTx.TxOutput.get(0).TransactionId, firstTx.TxOutput.get(0)); //its important to store our first transaction in the UTXOs list.
		System.out.println("First block creation and mining... ");
		basicblock newBlock = new basicblock("0");
		newBlock.addTransaction(firstTx);
		insertBlock(newBlock);
		//testing
		//Adding first block, transfer valid funds from Account A to Account B 
		basicblock firstBlock = new basicblock(newBlock.outputHash);
		System.out.println("\nAccountA's balance is: " + AccountA.accountBal());
		System.out.println("\nAccountA is sending funds of 40 to AccountB...");
		firstBlock.addTransaction(AccountA.sendAmount(AccountB.keyPub, 40f));
		insertBlock(firstBlock);
		System.out.println("\nAccountA's balance is: " + AccountA.accountBal());
		System.out.println("\nAccountB's balance is: " + AccountB.accountBal());
		//Adding second block, transfer invalid funds from Account A to Account B 
		basicblock secondBlock = new basicblock(firstBlock.outputHash);
		System.out.println("\nAccountA's balance is: " + AccountA.accountBal());
		System.out.println("\nAccountA is sending funds of 140(More than what it has) to AccountB...");
		secondBlock.addTransaction(AccountA.sendAmount(AccountB.keyPub, 140f));
		insertBlock(secondBlock);
		System.out.println("\nAccountA's balance is: " + AccountA.accountBal());
		System.out.println("\nAccountB's balance is: " + AccountB.accountBal());
		//Adding third block, transfer valid funds from Account B to Account A 
		basicblock thirdBlock = new basicblock(secondBlock.outputHash);
		System.out.println("\nAccountB's balance is: " + AccountB.accountBal());
		System.out.println("\nAccountB is sending funds of 30 to AccountA...");
		thirdBlock.addTransaction(AccountB.sendAmount(AccountA.keyPub, 30f));
		insertBlock(thirdBlock);
		System.out.println("\nAccountA's balance is: " + AccountA.accountBal());
		System.out.println("\nAccountB's balance is: " + AccountB.accountBal());
		checkChainValidity();
		/*
		 * //Test public and private keys
		 * 
		 * System.out.println("Private and public keys:");
		 * System.out.println(hashing.KeyToString(AccountA.keyPri));
		 * System.out.println(hashing.KeyToString(AccountA.keyPub)); //Create a test
		 * transaction from WalletA to walletB TransferHandler transaction = new
		 * TransferHandler(AccountA.keyPub, AccountB.keyPub, 5, null);
		 * transaction.createSig(AccountA.keyPri); //Verify the signature works and
		 * verify it from the public key System.out.println("Is signature verified");
		 * System.out.println(transaction.validateSig());
		 */	
	}
	public static Boolean checkChainValidity() {
		basicblock presentBlock; 
		basicblock beforeBlock;
		String targetHash = new String(new char[diffPoW]).replace('\0', '0');
		HashMap<String,OutputTransaction> unSpentTxsTemp = new HashMap<String,OutputTransaction>(); //a temporary working list of unspent transactions at a given block state.
		unSpentTxsTemp.put(firstTx.TxOutput.get(0).TransactionId, firstTx.TxOutput.get(0));
		
		//loop through blockchain to check hashes:
		for(int i=1; i < chainOfBlocks.size(); i++) {
			
			presentBlock = chainOfBlocks.get(i);
			beforeBlock = chainOfBlocks.get(i-1);
			//compare registered hash and calculated hash:
			if(!presentBlock.outputHash.equals(presentBlock.hashCalculater()) ){
				System.out.println("#Present Block hashes are equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!beforeBlock.outputHash.equals(presentBlock.inputHash) ) {
				System.out.println("#Before block output is not equal to present block input");
				return false;
			}
			//check if hash is solved
			if(!presentBlock.outputHash.substring( 0, diffPoW).equals(targetHash)) {
				System.out.println("#Block is not mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			OutputTransaction storeOpTx;
			for(int t=0; t <presentBlock.transfers.size(); t++) {
				TransferHandler currentTransaction = presentBlock.transfers.get(t);
				
				if(!currentTransaction.validateSig()) {
					System.out.println("# Transfer(" + t + ") signature is not valid");
					return false; 
				}
				if(currentTransaction.getInputAmount() != currentTransaction.getOutputAmount()) {
					System.out.println("#Input Amount is not equal to the outpit amount on Tx(" + t + ")");
					return false; 
				}
				
				for(InputTransaction inTx: currentTransaction.TxInput) {	
					storeOpTx = unSpentTxsTemp.get(inTx.transactionIdOp);
					
					if(storeOpTx == null) {
						System.out.println("#Input Tx(" + t + ") is Missing");
						return false;
					}
					
					if(inTx.Unspent.amount != storeOpTx.amount) {
						System.out.println("#Input Tx(" + t + ") value is not a valid one");
						return false;
					}
					
					unSpentTxsTemp.remove(inTx.transactionIdOp);
				}
				
				for(OutputTransaction outTx: currentTransaction.TxOutput) {
					unSpentTxsTemp.put(outTx.TransactionId, outTx);
				}
				
				if( currentTransaction.TxOutput.get(0).recievier != currentTransaction.reciversKey) {
					System.out.println("#Transfer(" + t + ") output recivers key is not matching with actual output receivers key");
					return false;
				}
				if( currentTransaction.TxOutput.get(1).recievier != currentTransaction.sendersKey) {
					System.out.println("#Transfer(" + t + ") Senders Key is not matching.");
					return false;
				}
				
			}
			
		}
		System.out.println("This Blockchain is  valid one");
		return true;
	}
	public static void insertBlock(basicblock blockNew) {
		blockNew.blockMining(diffPoW);
		chainOfBlocks.add(blockNew);
	}
}