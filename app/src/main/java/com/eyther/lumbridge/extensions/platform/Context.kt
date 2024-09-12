package com.eyther.lumbridge.extensions.platform

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent

/**
 * The MIME type for sending an email.
 *
 * Check the mime type reference for more information on the type of data that can be sent, and why we're using
 * `vnd.android.cursor.item/email` as the type. We could also use `message/rfc822`.
 * [Mime Types](https://developer.android.com/guide/topics/providers/content-provider-basics#MIMETypeReference)
 */
private const val EMAIL_MIME_TYPE = "vnd.android.cursor.item/email"

/**
 * Represents the status of sending an email in the [sendEmail] function.
 *
 * - [SendEmailStatus.Success] - The email was sent successfully.
 * - [SendEmailStatus.NoEmailAppAvailable] - No email app is available to send the email.
 * - [SendEmailStatus.UnexpectedError] - An unexpected error occurred while sending the email.
 */
sealed interface SendEmailStatus {
    data object Success : SendEmailStatus
    data object NoEmailAppAvailable : SendEmailStatus
    data object UnexpectedError : SendEmailStatus
}

/**
 * Opens the email app with the provided email addresses and subject.
 *
 * @param emailTo The email addresses to send the email to. e.g emailTest@gmail.com
 * @param subject The subject of the email. e.g Hello World!
 * @param body The body of the email. e.g This is the body of the email
 * @param selectorTitle Text to show in the app selector. e.g Send mail using
 *
 * @return [SendEmailStatus] representing the status of sending the email.
 */
fun Context.sendEmail(
    vararg emailTo: String,
    subject: String,
    body: String,
    selectorTitle: String
): SendEmailStatus {
    return try {
        // Create an intent to send an email
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = EMAIL_MIME_TYPE
            putExtra(Intent.EXTRA_EMAIL, emailTo)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        // Open the chooser dialog to select an email app
        startActivity(Intent.createChooser(intent, selectorTitle))

        // Return the success status
        SendEmailStatus.Success
    } catch (e: ActivityNotFoundException) {
        // There is no email app available to send the email
        SendEmailStatus.NoEmailAppAvailable
    } catch (t: Throwable) {
        // An unexpected error occurred while sending the email
        SendEmailStatus.UnexpectedError
    }
}
