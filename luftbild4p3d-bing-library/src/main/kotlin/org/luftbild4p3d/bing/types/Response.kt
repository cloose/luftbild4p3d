package org.luftbild4p3d.bing.types

data class Response(val authenticationResultCode: String, val brandLogoUri: String, val copyright: String, val resourceSets: List<ResourceSet>)