package pl.qone.payload.response;

public class EventStatementResponse {
	private String name;
	private Double averageRate;
	private String contestantNumbers;
	private String organizer;
	private double contestantPercentage;
	private String company;
	
	public EventStatementResponse(String name, Double averageRate, String contestantNumbers, String organizer, double contestantPercentage, String company) {
		this.name = name;
		this.averageRate = averageRate;
		this.contestantNumbers = contestantNumbers;
		this.organizer = organizer;
		this.contestantPercentage = contestantPercentage;
		this.company = company;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getAverageRate() {
		return averageRate;
	}
	public void setAverageRate(Double averageRate) {
		this.averageRate = averageRate;
	}
	public String getContestantNumbers() {
		return contestantNumbers;
	}
	public void setContestantNumbers(String contestantNumbers) {
		this.contestantNumbers = contestantNumbers;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public double getContestantPercentage() {
		return contestantPercentage;
	}
	public void setContestantPercentage(double contestantPercentage) {
		this.contestantPercentage = contestantPercentage;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	
}
