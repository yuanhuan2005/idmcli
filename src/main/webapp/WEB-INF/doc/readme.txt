Usage:
    1. idm [OPTIONS]...
        OPTIONS: Options for IDM CLI.
            -i/--install: Install IDM CLI.
            -u/--uninstall: Uninstall IDM CLI.
            -l/--list-all-apis: List all IDM interfaces.
            -h/--help: Display this help and exit.

    2. idm (IDM_API_NAME) [API_OPTIONS/API_PARAMETERS]...
        IDM_API_NAME: The API Name of IDM. Case Sensitive.
        API_OPTIONS: Options for this IDM API.
            -f/--format-json: Convert json result to human readable format.
            -h/--help: Display IDM API help and exit.
        API_PARAMETERS: The API parameter(s) of this IDM interface.
            See API help infomation.

Description: 
    Call IDM interface by specifying the API name and parameters.

Samples: 
    1. idm -l
       List all IDM interfaces.
    2. idm -h
       Display this help and exit.
    3. idm createUser -h
       How to create a user.
    4. idm createUser userName Bob password 123456
       Create a user with name Bob and password 123456
    5. idm listUsers
       List all users of this account.

Exit status: 
    0  if OK,
    1  if minor problems (e.g., cannot access subdirectory),
    2  if serious trouble (e.g., cannot access command-line argument).

Author: 
    Yuan Huan

Bug report: 
    yuanhuan@tcl.com
