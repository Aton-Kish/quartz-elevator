#!/bin/sh

set -eux

if [ ! -e ${MOD_FILE} ]; then
    echo "file not found: ${MOD_FILE}"
    exit 1
fi

METADATA=$(cat << EOS | jq -c .
{
  "changelog": "[v${MOD_VERSION}](https://github.com/Aton-Kish/quartz-elevator/releases/tag/v$(echo ${MOD_VERSION} | jq -Rr @uri))",
  "changelogType": "markdown",
  "displayName": "Quartz Elevator v${MOD_VERSION}",
  "gameVersions": ${GAME_VERSIONS},
  "releaseType": "release",
  "relations": {
    "projects": [
      {
        "slug": "fabric-api",
        "type": "requiredDependency"
      },
      {
        "slug": "cloth-config",
        "type": "embeddedLibrary"
      },
      {
        "slug": "modmenu",
        "type": "optionalDependency"
      }
    ]
  }
}
EOS
)
echo ${METADATA}

set +x

curl -s -f \
    -X POST \
    -H "X-Api-Token: ${API_TOKEN}" \
    -F "file=@${MOD_FILE}" \
    -F "metadata=${METADATA}" \
    https://minecraft.curseforge.com/api/projects/${PROJECT_ID}/upload-file
