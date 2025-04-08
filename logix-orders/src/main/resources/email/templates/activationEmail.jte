@param com.vazant.logix.orders.application.service.shared.email.model.ActivationEmailModel variables

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>Activate Your Account</title>
    <style>
      body {
        font-family: Helvetica, Arial, sans-serif;
        background-color: #f7fafc;
        color: #333;
        font-size: 16px;
        line-height: 1.6;
        padding: 20px;
      }

      .email-container {
        max-width: 600px;
        margin: 0 auto;
        background: #ffffff;
        border-radius: 8px;
        border: 1px solid #e2e8f0;
        padding: 32px;
      }

      h1 {
        font-size: 24px;
        margin-bottom: 24px;
        color: #2d3748;
      }

      p {
        margin: 0 0 16px;
      }

      .button {
        display: inline-block;
        background-color: #0d6efd;
        color: #ffffff;
        padding: 12px 24px;
        border-radius: 6px;
        text-decoration: none;
        font-weight: 500;
      }

      .footer {
        margin-top: 40px;
        font-size: 13px;
        color: #718096;
        text-align: center;
      }
    </style>
</head>
<body>
<div class="email-container">
    <h1>Welcome, @{variables.username()}!</h1>

    <p>We've created an account for you in the system.</p>

    <p><strong>Temporary Password:</strong> @{variables.tempPassword()}</p>

    <p>To activate your account, please follow this link:</p>

    <p>
        <a class="button" href="@{variables.activationLink()}">Activate Account</a>
    </p>

    <p>If you didn’t request this email, you can ignore it.</p>

    <div class="footer">
        This message was sent automatically. Please do not reply.
    </div>
</div>
</body>
</html>
