package bee;

public class UpdateAudit {
	

	private String oldCategory;
	private Long eventId;
	private Long numId;
	
	private String newCategory;
	private Double tranAmount;
	private String accountNo;
	private Long tranDate;
	private String merchant;
	
	public Long getNumId() {
		return numId;
	}
	
	public void setNumId(Long numId) {
		this.numId = numId;
	}
	
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getNewCategory() {
		return newCategory;
	}
	public String getOldCategory() {
		return oldCategory;
	}
	public Double getTranAmount() {
		return tranAmount;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public Long getTranDate() {
		return tranDate;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setNewCategory(String newCategory) {
		this.newCategory = newCategory;
	}
	public void setOldCategory(String oldCategory) {
		this.oldCategory = oldCategory;
	}
	public void setTranAmount(Double tranAmount) {
		this.tranAmount = tranAmount;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public void setTranDate(Long tranDate) {
		this.tranDate = tranDate;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

}
