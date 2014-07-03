<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4"
                                       page-height="297mm"
                                       page-width="210mm"
                                       margin-top="20mm"
                                       margin-bottom="20mm"
                                       margin-left="25mm"
                                       margin-right="25mm">
                    <fo:region-body margin-top="25mm"/>
                    <fo:region-after region-name="footer" extent="15mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <fo:static-content flow-name="footer">
                    <fo:block text-align="center">
                        Page <fo:page-number/>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template match="header">
        <fo:block>
            Title: <xsl:value-of select="title"/>
        </fo:block>
        <fo:block>
            Author: <xsl:value-of select="author/fullname"/> (<xsl:value-of select="author/mailaddress"/>)
        </fo:block>
        <fo:block>
            Date: <xsl:value-of select="creationdate"/>
        </fo:block>
    </xsl:template>

    <xsl:template match="content">
        <fo:block space-after="8pt"
                  space-before="16pt">
            Content: <xsl:value-of select="text"/>
        </fo:block>
    </xsl:template>
</xsl:stylesheet>