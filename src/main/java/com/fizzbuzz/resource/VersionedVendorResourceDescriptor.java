package com.fizzbuzz.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.util.Series;

import com.fizzbuzz.model.Version;

public class VersionedVendorResourceDescriptor {
    private final String mResourceType;
    private final String mVendor;
    private final Version mVersion;

    // "vnd." followed by
    // some word characters, followed by
    // a period, followed by
    // some word characters potentially mixed with hyphens (e.g. foo-bar), followed by
    // "-v", followed by
    // some numbers
    static private final Pattern mPattern = Pattern.compile("vnd\\.(\\w+)\\.([\\w-]+)-v(\\d+)");

    public VersionedVendorResourceDescriptor(final String resourceType, final String vendor, final Version version) {
        mResourceType = checkNotNull(resourceType, "resourceType");
        mVendor = checkNotNull(vendor, "vendor");
        mVersion = checkNotNull(version, "version");
    }

    public static VersionedVendorResourceDescriptor parseMediaType(final MediaType m) {
        // find the version number in the media subtype string
        Matcher matcher = mPattern.matcher(m.getSubType());
        if (!matcher.find()) {
            throw new IllegalArgumentException("media type is not a versioned vendor-specific media type string");
        }
        String vendor = matcher.group(1);
        String resourceType = matcher.group(2);
        String major = matcher.group(3);
        String minor = "*";
        // find the "level" parameter value
        for (Parameter p : m.getParameters()) {
            if (p.getName().equals("level"))
                minor = p.getValue();
        }
        return new VersionedVendorResourceDescriptor(resourceType, vendor, new Version(major, minor));
    }

    public MediaType toMediaType(String format) {
        StringBuilder sb = new StringBuilder("application/vnd.");
        sb.append(mVendor)
                .append(mResourceType)
                .append("-v")
                .append(mVersion.getMajor());
        if (format != null && format.length() > 0)
            sb.append("+")
                    .append(format);

        Series<Parameter> params = new Series<Parameter>(Parameter.class);
        params.add(new Parameter("level", mVersion.getMinor()));

        return new MediaType(sb.toString(), params);
    }

    public Version getVersion() {
        return mVersion;
    }
}
