#!/bin/bash

# Navigate to the resources directory
cd /Users/saurabh/Downloads/mysql5/app/src/main/res

# Convert PNG to WebP with 80% quality
for png in $(find . -type f -name '*.png'); do
    webp_path="${png%.*}.webp"
    
    # Convert to WebP
    cwebp -q 80 "$png" -o "$webp_path"
    
    # Remove original PNG if conversion is successful
    if [ $? -eq 0 ]; then
        rm "$png"
        echo "Converted $png to $webp_path"
    fi
done
