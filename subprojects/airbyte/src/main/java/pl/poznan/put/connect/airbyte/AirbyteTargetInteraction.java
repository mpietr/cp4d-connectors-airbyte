/* *************************************************** */

/* (C) Copyright IBM Corp. 2022                        */

/* *************************************************** */
package pl.poznan.put.connect.airbyte;

import com.ibm.connect.sdk.api.Record;
import com.ibm.connect.sdk.api.RowBasedTargetInteraction;
import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightAssetDescriptor;

@SuppressWarnings({ "PMD.AvoidDollarSigns", "PMD.ClassNamingConventions" })
/*
  For now, source-only interactions.
 */
public class AirbyteTargetInteraction extends RowBasedTargetInteraction<AirbyteConnector>
{
    protected AirbyteTargetInteraction(AirbyteConnector connector, CustomFlightAssetDescriptor asset)
    {
        super();
        this.setConnector(connector);
        this.setAsset(asset);
    }

    @Override
    public void putRecord(Record record)
    {
    }

    @Override
    public void close()
    {
    }

    @Override
    public CustomFlightAssetDescriptor putSetup()
    {
        return null;
    }

    @Override
    public CustomFlightAssetDescriptor putWrapup()
    {
        return null;
    }

}
