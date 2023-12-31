SUMMARY = "OP-TEE sanity testsuite"
DESCRIPTION = "Open Portable Trusted Execution Environment - Test suite"
HOMEPAGE = "https://www.op-tee.org/"

LICENSE = "BSD-2-Clause & GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/LICENSE.md;md5=daa2bcccc666345ab8940aab1315a4fa"

inherit python3native ptest
require optee.inc

DEPENDS = "optee-client optee-os python3-cryptography-native"

SRC_URI = "git://github.com/OP-TEE/optee_test.git;branch=master;protocol=https \
           file://run-ptest \
          "

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

EXTRA_OEMAKE += "TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
                 O=${B} \
               "

do_compile() {
    cd ${S}
    # Top level makefile doesn't seem to handle parallel make gracefully
	export CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_HOST}"
	kludge_openssl
    oe_runmake xtest
    oe_runmake ta
    oe_runmake test_plugin
}
do_compile[cleandirs] = "${B}"

do_install () {
    install -D -p -m0755 ${B}/xtest/xtest ${D}${bindir}/xtest

    # install path should match the value set in optee-client/tee-supplicant
    # default TEEC_LOAD_PATH is /lib
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz/
    install -D -p -m0444 ${B}/ta/*/*.ta ${D}${nonarch_base_libdir}/optee_armtz/
    mkdir -p ${D}${libdir}/tee-supplicant/plugins
    install -D -p -m0444 ${B}/supp_plugin/*.plugin ${D}${libdir}/tee-supplicant/plugins/
}

FILES:${PN} += "${nonarch_base_libdir}/optee_armtz/ \
                ${libdir}/tee-supplicant/plugins/ \
               "

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
