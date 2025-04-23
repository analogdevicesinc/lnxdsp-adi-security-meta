
require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc

COMPATIBLE_MACHINE = "adsp-sc598-som-ezkit"

TFA_GIT_URI ?= "git://github.com/analogdevicesinc/trusted-firmware-a.git"
TFA_GIT_PROTOCOL ?= "https"
TFA_GIT_BRANCH ?= "develop/upstream"

SRC_URI = "${TFA_GIT_URI};protocol=${TFA_GIT_PROTOCOL};name=tfa;branch=${TFA_GIT_BRANCH}"

SRCREV_FORMAT = "tfa"

SRCREV_tfa = "${AUTOREV}"

LIC_FILES_CHKSUM += "file://docs/license.rst;md5=1118e32884721c0be33267bd7ae11130"

S = "${WORKDIR}/git"

TFA_PLATFORM = "adsp_sc598"
TFA_BUILD_TARGET = "bl31"
TFA_SPD = "opteed"
