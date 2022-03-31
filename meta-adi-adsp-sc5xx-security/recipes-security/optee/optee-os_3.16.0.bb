# Based on OP-TEE recipe for dunfell, but that one doesn't work out of the box for 3.16
# Several options are configurable in local.conf

SUMMARY = "OP-TEE Trusted OS"
DESCRIPTION = "Open Portable Trusted Execution Environment - Trusted side of the TEE"
HOMEPAGE = "https://www.op-tee.org/"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

PV = "3.16.0+git${SRCPV}"

inherit deploy python3native

DEPENDS = "python3-cryptography-native python3-pycryptodome-native python3-pycryptodomex-native python3-pyelftools-native"

OPTEE_OS_GIT_URI ?= "git://git@src.timesys.com/services/analog-devices/analog-devices-new-board-bringup-1/optee-os.git"
OPTEE_OS_GIT_PROTOCOL ?= "ssh"
OPTEE_OS_CORE_LOG_LEVEL ?= "1"
OPTEE_OS_ENABLE_TESTS ?= "n"

SRC_URI = "${OPTEE_OS_GIT_URI};branch=develop/sc598;protocol=${OPTEE_OS_GIT_PROTOCOL}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = " \
    PLATFORM=adi \
	ARM=arm \
	CROSS_COMPILE=${HOST_PREFIX} \
    CROSS_COMPILE_core=${HOST_PREFIX} \
    CROSS_COMPILE_ta_arm64=${HOST_PREFIX} \
	CFG_TEE_CORE_LOG_LEVEL=${OPTEE_OS_CORE_LOG_LEVEL} \
	CFG_TEE_TA_LOG_LEVEL=${OPTEE_OS_CORE_LOG_LEVEL} \
	CFG_ENABLE_EMBEDDED_TESTS=${OPTEE_OS_ENABLE_TESTS} \
	LDFLAGS= \
"

do_compile() {
	export CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST}"
    oe_runmake all
}

do_install() {
    #install TA devkit
    install -d ${D}${includedir}/optee/export-user_ta/
    for f in ${B}/out/arm-plat-adi/export-ta_arm64/* ; do
        cp -aR $f ${D}${includedir}/optee/export-user_ta/
    done
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0755 ${B}/out/arm-plat-adi/core/tee.bin ${DEPLOYDIR}/
}

addtask deploy after do_install before do_build

FILES_${PN}-dev = "${includedir}/optee/"

INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
