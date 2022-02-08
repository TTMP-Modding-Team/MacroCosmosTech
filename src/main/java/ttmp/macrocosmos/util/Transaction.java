package ttmp.macrocosmos.util;

import javax.annotation.Nullable;

/**
 * Simple instance for wrapping any transactions with possibility of failure.<br>
 * <p>
 * Transactions have two possible state - failure and success.<br>
 * Succeed transactions may have some action attached. They will not be executed until run manually.
 */
public abstract class Transaction{
	private static final Transaction FAILURE = new Transaction(){
		@Override public boolean isSuccess(){
			return false;
		}
		@Override public void run(){}
	};
	private static final Transaction SUCCESS_NO_ACTION = new Transaction(){
		@Override public void run(){}
	};

	/**
	 * @return A failed transaction.
	 */
	public static Transaction fail(){
		return FAILURE;
	}
	/**
	 * @return A successful transaction, with no action attached.
	 */
	public static Transaction success(){
		return SUCCESS_NO_ACTION;
	}
	/**
	 * @return A successful transaction with the provided action attached.
	 */
	public static Transaction success(@Nullable Runnable action){
		return action==null ? SUCCESS_NO_ACTION : new Action(action);
	}
	/**
	 * @return Multiple transactions wrapped into one transaction. It will be considered success when every provided transaction is success.
	 */
	public static Transaction multiple(Transaction... transactions){
		return new Multi(transactions);
	}

	public boolean isSuccess(){
		return true;
	}
	public abstract void run();

	public final boolean runIfSuccess(){
		if(isSuccess()){
			run();
			return true;
		}else return false;
	}

	private static final class Action extends Transaction{
		private final Runnable action;

		public Action(Runnable action){
			this.action = action;
		}

		@Override public void run(){
			if(this.action!=null) this.action.run();
		}
	}

	private static final class Multi extends Transaction{
		private final Transaction[] transactions;

		private Multi(Transaction... transactions){
			this.transactions = transactions;
		}

		@Override public boolean isSuccess(){
			for(Transaction transaction : transactions)
				if(!transaction.isSuccess()) return false;
			return true;
		}
		@Override public void run(){
			if(!isSuccess()) return;
			for(Transaction transaction : transactions)
				transaction.run();
		}
	}
}
