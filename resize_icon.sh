#!/bin/bash

# Source icon
SOURCE="/Users/tiaastor/Downloads/DnDDiceRollerIcon.png"

# Dimensions for different densities
declare -A SIZES=(
    ["mdpi"]="48x48"
    ["hdpi"]="72x72"
    ["xhdpi"]="96x96"
    ["xxhdpi"]="144x144"
    ["xxxhdpi"]="192x192"
)

# Resize for each density
for density in "${!SIZES[@]}"; do
    sips -z "${SIZES[$density]%%x*}" "${SIZES[$density]##*x}" "$SOURCE" --out "app/src/main/res/mipmap-$density/ic_launcher.png"
    cp "app/src/main/res/mipmap-$density/ic_launcher.png" "app/src/main/res/mipmap-$density/ic_launcher_round.png"
done
