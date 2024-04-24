#!/bin/bash

# Get the current directory path
current_dir=$(pwd)

# Name of the variable to store the path
var_name="ATCS_DIR_PATH"

# Check if the variable is already set in bash_profile or zshrc
if ! grep -q "^export $var_name=" ~/.bash_profile ~/.zshrc; then
    # The variable isn't set, so append it to both files
    echo "export $var_name=\"$current_dir\"" >> ~/.bash_profile
    echo "export $var_name=\"$current_dir\"" >> ~/.zshrc
    # Source the profiles to update current session
    source ~/.bash_profile
    source ~/.zshrc

    echo "$var_name set to $current_dir"

else
    echo "$var_name is already set in your environment files."
fi

# navigate to project directory
cd "${!var_name}" || { echo "Failed to change directory to ${!var_name}"; exit 1; }

# set up the venv
chmod 777 ./scripts/set-up-env.sh
./scripts/set-up-env.sh || { echo "Failed to set up venv "; exit 1; }

# install requirements
chmod 777 ./scripts/install-requirements.sh
./scripts/install-requirements.sh || { echo "Failed to change launch install-requirements script "; exit 1; }