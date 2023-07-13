FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:adsp-sc5xx-optee = " \
	file://0001-enable-optee-configuration.patch \
"
