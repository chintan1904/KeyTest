package beans;

import java.lang.reflect.Method;
import common.Operation;
import common.Result;

public class Action {

	private String keyword;
	private String sourceElement;
	private String data;
	private String stepId;
	private Result result;
	private StringBuilder message = new StringBuilder();

	public Action(String keyword, String sourceElement, String data, String stepId) {
		setKeyword(keyword);
		setSourceElement(sourceElement);
		setData(data);
		setStepId(stepId);
	}

	public void execute() {

		boolean isMethodFound = false;
		Method methods[] = Operation.class.getMethods();
		for (Method method : methods) {
			if (method.getName().equalsIgnoreCase(keyword)) {
				isMethodFound = true;
				try {
					method.invoke(null, sourceElement, data);
					appendMessage("Successfully executed action : " + keyword + " ,on Source Element : " + sourceElement
							+ " , with data : " + data);
					setResult(Result.PASS);
				} catch (Exception e) {
					appendMessage("Failure while executing method : " + keyword + " on page element : " + sourceElement
							+ " with data : " + data);
					appendMessage(System.lineSeparator());
					appendMessage(e.getMessage());
					setResult(Result.FAIL);
				}
			}
		}
		if (!isMethodFound) {
			appendMessage("Required action: " + keyword + " not found");
			setResult(Result.FAIL);
		}
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSourceElement() {
		return sourceElement;
	}

	public void setSourceElement(String sourceElement) {
		this.sourceElement = sourceElement;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getMessage() {
		return message.toString();
	}

	public void appendMessage(String error) {
		this.message.append(error);
	}

}
