# Based on OP-TEE recipe for dunfell, but that one doesn't work out of the box for 3.16
# Several options are configurable in local.conf

SUMMARY = "OP-TEE Trusted OS"
DESCRIPTION = "Open Portable Trusted Execution Environment - Trusted side of the TEE"
HOMEPAGE = "https://www.op-tee.org/"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

PV = "3.16.0+git${SRCPV}"

inherit deploy python3native

DEPENDS = "python3-cryptography-native python3-pycryptodome-native python3-pycryptodomex-native python3-pyelftools-native openssl-native"

OPTEE_OS_GIT_URI ?= "git://git@src.timesys.com/services/analog-devices/analog-devices-new-board-bringup-1/optee-os.git"
OPTEE_OS_GIT_PROTOCOL ?= "ssh"
OPTEE_OS_CORE_LOG_LEVEL ?= "1"
OPTEE_OS_ENABLE_TESTS ?= "n"

SRC_URI = " \
	${OPTEE_OS_GIT_URI};branch=develop/sc598;protocol=${OPTEE_OS_GIT_PROTOCOL} \
	file://libotp.a \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

OPTEE_PLATFORM ?= "adi"
OPTEE_FLAVOR ?= "adsp_sc598"

OPTEE_TA_TYPE ?= "ta_arm64"
OPTEE_TA_TYPE:armv7a := "ta_arm32"

EXTRA_OEMAKE = " \
    PLATFORM=${OPTEE_PLATFORM} \
	PLATFORM_FLAVOR=${OPTEE_FLAVOR} \
	ARM=arm \
	CROSS_COMPILE=${HOST_PREFIX} \
    CROSS_COMPILE_core=${HOST_PREFIX} \
    CROSS_COMPILE_${OPTEE_TA_TYPE}=${HOST_PREFIX} \
	CFG_TEE_CORE_LOG_LEVEL=${OPTEE_OS_CORE_LOG_LEVEL} \
	CFG_TEE_TA_LOG_LEVEL=${OPTEE_OS_CORE_LOG_LEVEL} \
	CFG_ENABLE_EMBEDDED_TESTS=${OPTEE_OS_ENABLE_TESTS} \
	LDFLAGS= \
"

do_configure() {
	cp ${WORKDIR}/libotp.a ${S}/libotp.a
}

do_compile() {
	export CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST}"
	# see: poky/openssl_3.x.bb do_install:append:class-native for how the openssl
	# wrapper is supposed to be generated, which doesn't seem to propagate to
	# python3-cryptography-native modules properly
	export OPENSSL_CONF="${STAGING_LIBDIR_NATIVE}/ssl-3/openssl.cnf"
	export OPENSSL_ENGINES="${STAGING_LIBDIR_NATIVE}/engines-3"
	export OPENSSL_MODULES="${STAGING_LIBDIR_NATIVE}/ossl-modules"
	export SSL_CERT_DIR="${STAGING_LIBDIR_NATIVE}/ssl-3/certs"
    oe_runmake all
}

do_install() {
    #install TA devkit
    install -d ${D}${includedir}/optee/export-user_ta/
    for f in ${B}/out/arm-plat-${OPTEE_PLATFORM}/export-${OPTEE_TA_TYPE}/* ; do
        cp -aR $f ${D}${includedir}/optee/export-user_ta/
    done
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0755 ${B}/out/arm-plat-${OPTEE_PLATFORM}/core/tee.bin ${DEPLOYDIR}/
}

addtask deploy after do_install before do_build

FILES:${PN}-dev = "${includedir}/optee/"

INSANE_SKIP:${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
