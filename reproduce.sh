#!/usr/bin/env bash

docker compose -f "docker-compose.yaml" --project-name test up --wait;

for i in {1..100}; do
	echo "Running iteration $i"
	gradle test
    echo "Counting videos. Expected: ${i}"
    video_count_with_headers="$(docker exec test-firefox_video-1 ls -alh /videos | wc -l)"
    video_count="$(( video_count_with_headers - 3 ))"
    echo "Actual video count: ${video_count}"
    if [ "${video_count}" -ne "${i}" ]; then
        echo "Video count mismatch. Expected: ${i}, Actual: ${video_count}"
        break
    fi
done

# docker compose -f "docker-compose.yaml" --project-name test down;