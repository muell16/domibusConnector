<!DOCTYPE html>
<html>
    <head>
        <title>WebDobby Login</title>
        <meta charset="UTF-8"></meta>
        <meta http-equiv="x-ua-compatible" content="ie=edge"></meta>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"></meta>
        <link rel="stylesheet" type="text/css" href="styles/login.css">
    </head>
    <body onload='document.f.username.focus();'>

        <div class="ui-login-container">
            <span class="ui-login-title">WebDobby</span>
            <div class="ui-login-form">
                <form name='f' action='/login' method='POST'>
                    <table id="form-table">
                        <tr id="form-username">
                            <td>User</td>
                        </tr>
                        <tr>
                            <td><input type='text' name='username' value=''></td>
                        </tr>
                        <tr>
                            <td>Password</td>
                        </tr>
                        <tr>
                            <td><input type='password' name='password'/></td>
                        </tr>
                        <tr>
                            <td colspan='2'><input class="ui-login-form-button" name="submit" type="submit" value="Login"/></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>

        <script type="text/javascript">
            function isError() {
                if (window.location.search) {
                    var queries = window.location.search.substring(1).split("&");
                    for (var i = 0; i < queries.length; i++) {
                        var query = queries[i];
                        if (query.indexOf("error=") > -1) {
                            var value = query.replace("error=", "");
                            return value == "true";
                        }
                    }
                }

                return false;
            }

            var loginError = isError();
            if (loginError) {
                var errorMsg = document.createElement("div");
                errorMsg.appendChild(document.createTextNode("Falscher Benutzername oder Passwort"));
                errorMsg.className = "ui-login-form-error";

                var container = document.f.parentNode;
                container.insertBefore(errorMsg, document.f);
            }

        </script>
    </body>
</html>
