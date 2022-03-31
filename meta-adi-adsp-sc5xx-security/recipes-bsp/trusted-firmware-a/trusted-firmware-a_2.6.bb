
require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc

COMPATIBLE_MACHINE = "adsp-sc598-som-ezkit"

TFA_GIT_URI ?= "git://git@src.timesys.com/services/analog-devices/analog-devices-new-board-bringup-1/arm-trusted-firmware.git"
TFA_GIT_PROTOCOL ?= "ssh"
TFA_GIT_BRANCH ?= "develop/sc598"

SRC_URI = "${TFA_GIT_URI};protocol=${TFA_GIT_PROTOCOL};name=tfa;branch=${TFA_GIT_BRANCH}"

SRCREV_FORMAT = "tfa"

SRCREV_tfa = "${AUTOREV}"

S = "${WORKDIR}/git"

TFA_PLATFORM = "adsp_sc598"
TFA_BUILD_TARGET = "bl31"
TFA_SPD = "opteed"
