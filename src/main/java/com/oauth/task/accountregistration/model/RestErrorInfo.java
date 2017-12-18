/**
 * 
 */
package com.oauth.task.accountregistration.model;

/**
 * @author SUSHREE
 *
 */
public class RestErrorInfo {

	public final String detail;
    public final String message;

    public RestErrorInfo(Exception ex, String detail) {
        this.message = ex.getLocalizedMessage();
        this.detail = detail;
    }
}
