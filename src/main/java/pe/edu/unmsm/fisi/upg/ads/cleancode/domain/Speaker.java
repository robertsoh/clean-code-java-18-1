package pe.edu.unmsm.fisi.upg.ads.cleancode.domain;

import java.util.Arrays;
import java.util.List;

import pe.edu.unmsm.fisi.upg.ads.cleancode.exceptions.NoSessionsApprovedException;
import pe.edu.unmsm.fisi.upg.ads.cleancode.exceptions.SpeakerDoesntMeetRequirementsException;

public class Speaker {
	private String firstName;
	private String lastName;
	private String email;
	private int experienceYears;
	private boolean hasBlog;
	private String blogURL;
	private WebBrowser browser;
	private List<String> certifications;
	private String employer;
	private int registrationFee;
	private List<Session> sessions;

	private boolean isApprovedSpeaker(){
	    boolean isApproved = false;
        List<String> domains = Arrays.asList("aol.com", "hotmail.com", "prodigy.com", "compuserve.com");
        List<String> employers = Arrays.asList("Pluralsight", "Microsoft", "Google", "Fog Creek Software", "37Signals", "Telerik");

        if (this.experienceYears > 10 || this.hasBlog || this.certifications.size() > 3 || employers.contains(this.employer)){
            isApproved = true;
        } else {
            String[] splitted = this.email.split("@");
            String emailDomain = splitted[splitted.length - 1];
            boolean isOldVersionInternetExplorer = browser.getName() == WebBrowser.BrowserName.InternetExplorer && browser.getMajorVersion() < 9;

            if ( !(domains.contains(emailDomain) || isOldVersionInternetExplorer )) {
                isApproved = true;
            }
        }
	    return isApproved;
    }

	public Integer register(IRepository repository) throws Exception {
		Integer speakerId = null;
		boolean appr = false;
		String[] oldTechnologies = new String[] { "Cobol", "Punch Cards", "Commodore", "VBScript" };

		if (this.firstName.isEmpty()) {
			throw new IllegalArgumentException("First Name is required");
		}
		if (this.lastName.isEmpty()) {
			throw new IllegalArgumentException("Last name is required.");
		}
		if (this.email.isEmpty()) {
			throw new IllegalArgumentException("Email is required.");
		}

		if ( !this.isApprovedSpeaker() ){
            throw new SpeakerDoesntMeetRequirementsException("Speaker doesn't meet our abitrary and capricious standards.");
        } else {
            if (this.sessions.size() != 0) {
                for (Session session : sessions) {
                    for (String tech : oldTechnologies) {
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
                if (this.experienceYears <= 1) {
                    this.registrationFee = 500;
                }
                else if (experienceYears >= 2 && experienceYears <= 3) {
                    this.registrationFee = 250;
                }
                else if (experienceYears >= 4 && experienceYears <= 5) {
                    this.registrationFee = 100;
                }
                else if (experienceYears >= 6 && experienceYears <= 9) {
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
        }

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

	public int getExperienceYears() {
		return experienceYears;
	}

	public void setExperienceYears(int experienceYears) {
		this.experienceYears = experienceYears;
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