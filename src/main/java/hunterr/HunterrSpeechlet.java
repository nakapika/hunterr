package hunterr;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;


public class HunterrSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(HunterrSpeechlet.class);

	// private AmazonDynamoDBClient amazonDynamoDBClient;

	private HunterrResponseUtil responseUtil;

	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		// if user said a one shot command that triggered an intent event,
		// it will start a new session, and then we should avoid speaking too
		// many words.
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		
		Intent intent = request.getIntent();
		
		if ("SearchJobIntent".equals(intent.getName())) {
			return HunterrResponseUtil.getStartJobTitleResponse(session);
		} else if ("ResponseIntent".equals(intent.getName())) {
			String lastQuestion = (String)session.getAttribute("lastQuestion");
			String response;
			
			switch(lastQuestion) {
				case "Job Title":
					response = intent.getSlot("jobtitle").getValue();
					return HunterrResponseUtil.getCompanyResponse(session, response);
				case "Company":
					response = intent.getSlot("company").getValue();
					return HunterrResponseUtil.getLocationResponse(session, response);
				case "Location": 
					response = intent.getSlot("locationcity").getValue();
					return HunterrResponseUtil.getJobTypeResponse(session, response);
				default:
					response = intent.getSlot("jobtype").getValue();
					return HunterrResponseUtil.getStopIntentResponse(session, response);
			}
		} else if ("AMAZON.YesIntent".equals(intent.getName())) {
			return HunterrResponseUtil.getYesIntentResponse(session);
		} else if ("AMAZON.NoIntent".equals(intent.getName())) {
			return HunterrResponseUtil.getNoIntentResponse(session);

		} else if ("AMAZON.StopIntent".equals(intent.getName())) {
			return HunterrResponseUtil.getStopIntentResponse(session);

		} else {
			throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
		}
	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		return responseUtil.getLaunchResponse(request, session);
	}

	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

}
