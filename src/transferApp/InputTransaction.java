package transferApp;

public class InputTransaction {

	public String transactionIdOp; //Reference to TransactionOutputs -> transactionId
	public OutputTransaction Unspent; //Contains the Unspent transaction output
	
	public InputTransaction(String transactionIdOp) {
		this.transactionIdOp = transactionIdOp;
	}
}
