package pe.edu.unmsm.fisi.upg.ads.dirtycode.domain;

import java.util.Arrays;
import java.util.List;

import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.NoSessionsApprovedException;
import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.SpeakerDoesntMeetRequirementsException;

public class Speaker {
	private String firstName;
	private String lastName;
	private String email;
	private int exp;
	private boolean hasBlog;
	private String blogURL;
	private WebBrowser browser;
	private List<String> certifications;
	private String employer;
	private int registrationFee;
	private List<Session> sessions;

	public Integer register(IRepository repository) throws Exception {
		Integer speakerId = null;
		boolean good = false;
		boolean appr = false;
		//String[] nt = new String[] { "Microservices", "Node.js", "CouchDB", "KendoUI", "Dapper", "Angular2" };
		String[] ot = new String[] { "Cobol", "Punch Cards", "Commodore", "VBScript" };
		
		//DEFECT #5274 DA 12/10/2012
		//We weren't filtering out the prodigy domain so I added it.
		List<String> domains = Arrays.asList("aol.com", "hotmail.com", "prodigy.com", "compuserve.com");
		
		if (!this.firstName.isEmpty()) {
			if (!this.lastName.isEmpty()) {
				if (!this.email.isEmpty()) {
					//put list of employers in array
					List<String> emps = Arrays.asList("Pluralsight", "Microsoft", "Google", "Fog Creek Software", "37Signals", "Telerik");
					
					//DFCT #838 Jimmy
					//We're now requiring 3 certifications so I changed the hard coded number. Boy, programming is hard.
					good = ((this.exp > 10 || this.hasBlog || this.certifications.size() > 3 || emps.contains(this.employer)));
					
					if (!good) {
						//need to get just the domain from the email
						String[] splitted = this.email.split("@");
						String emailDomain = splitted[splitted.length - 1];

						if (!domains.contains(emailDomain) && (!(browser.getName() == WebBrowser.BrowserName.InternetExplorer && browser.getMajorVersion() < 9)))
						{
							good = true;
						}
					}
					
					if (good) {
						//DEFECT #5013 CO 1/12/2012
						//We weren't requiring at least one session
						if (this.sessions.size() != 0) {
							for (Session session : sessions) {
								//for (String tech : nt) {
								//    if (session.getTitle().contains(tech) {
								//        session.setApproved(true);
								//        break;
								//    }
								//}
								for (String tech : ot) {
									if (session.getTitle().contains(tech) || session.getDescription().contains(tech)) {
										session.setApproved(false);
										break;
									} else {
										session.setApproved(true);
										appr = true;
									}
								}
								
							}
						} else {
							throw new IllegalArgumentException("Can't register speaker with no sessions to present.");
						}
						
						if (appr) {
							//if we got this far, the speaker is approved
							//let's go ahead and register him/her now.
							//First, let's calculate the registration fee.
							//More experienced speakers pay a lower fee.
							if (this.exp <= 1) {
								this.registrationFee = 500;
							}
							else if (exp >= 2 && exp <= 3) {
								this.registrationFee = 250;
							}
							else if (exp >= 4 && exp <= 5) {
								this.registrationFee = 100;
							}
							else if (exp >= 6 && exp <= 9) {
								this.registrationFee = 50;
							}
							else {
								this.registrationFee = 0;
							}
							
							//Now, save the speaker and sessions to the db.
							try {
								speakerId = repository.saveSpeaker(this);
							} catch (Exception e) {
								//in case the db call fails 
							}
						} else {
							throw new NoSessionsApprovedException("No sessions approved.");
						}
					} else {
						throw new SpeakerDoesntMeetRequirementsException("Speaker doesn't meet our abitrary and capricious standards.");
					}
				} else {
					throw new IllegalArgumentException("Email is required.");
				}				
			} else {
				throw new IllegalArgumentException("Last name is required.");
			}			
		} else {
			throw new IllegalArgumentException("First Name is required");
		}
			
		//if we got this far, the speaker is registered.
		return speakerId;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public boolean isHasBlog() {
		return hasBlog;
	}

	public void setHasBlog(boolean hasBlog) {
		this.hasBlog = hasBlog;
	}

	public String getBlogURL() {
		return blogURL;
	}

	public void setBlogURL(String blogURL) {
		this.blogURL = blogURL;
	}

	public WebBrowser getBrowser() {
		return browser;
	}

	public void setBrowser(WebBrowser browser) {
		this.browser = browser;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public int getRegistrationFee() {
		return registrationFee;
	}

	public void setRegistrationFee(int registrationFee) {
		this.registrationFee = registrationFee;
	}
}