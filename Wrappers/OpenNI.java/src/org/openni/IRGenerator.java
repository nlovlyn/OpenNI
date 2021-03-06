/*****************************************************************************
*                                                                            *
*  OpenNI 1.x Alpha                                                          *
*  Copyright (C) 2012 PrimeSense Ltd.                                        *
*                                                                            *
*  This file is part of OpenNI.                                              *
*                                                                            *
*  Licensed under the Apache License, Version 2.0 (the "License");           *
*  you may not use this file except in compliance with the License.          *
*  You may obtain a copy of the License at                                   *
*                                                                            *
*      http://www.apache.org/licenses/LICENSE-2.0                            *
*                                                                            *
*  Unless required by applicable law or agreed to in writing, software       *
*  distributed under the License is distributed on an "AS IS" BASIS,         *
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
*  See the License for the specific language governing permissions and       *
*  limitations under the License.                                            *
*                                                                            *
*****************************************************************************/
package org.openni;

/**
 * Generates IR Image data. <BR><BR>
 * 
 * This generator extends the MapGenerator to allow generation of IR image data.  This is similar
 * to the data generated by an RGB camera, but refers to the monochrome data generated by an IR 
 * CMOS.  <BR><BR>
 * 
 * On the PrimeSense devices, the IR data is the raw IR images that are used to generate the depth map.  
 *
 */
public class IRGenerator extends MapGenerator
{
	/**
	 * Constructor, creates a new IRGenerator object, with specified nodeHandle, from the specified Context.
	 * @param context OpenNI context that contains the desired generator
	 * @param nodeHandle Node handle of the generator to create
	 * @param addRef Whether to register this nodeHandle
	 * @throws GeneralException This function opens a hardware sensor, so exceptions are possible
	 */
	IRGenerator(Context context, long nodeHandle, boolean addRef) throws GeneralException
	{
		super(context, nodeHandle, addRef);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor, creates a new IRGenerator from the specified OpenNI context, using a query rather than a specified 
	 * nodeHandle.  Stores any enumeration errors in the provided EnumerationErrors object.
	 * @param context OpenNI Context to search for an IR generator in
	 * @param query Query object to filter search results
	 * @param errors Place to store any enumeration errors that occur
	 * @return Resulting IR Generator
	 * @throws GeneralException This function opens a hardware sensor, so exceptions are possible
	 */
	public static IRGenerator create(Context context, Query query, EnumerationErrors errors) throws GeneralException
	{
		OutArg<Long> handle = new OutArg<Long>();
		int status = NativeMethods.xnCreateIRGenerator(context.toNative(), handle,
			query == null ? 0 : query.toNative(),
			errors == null ? 0 : errors.toNative());
		WrapperUtils.throwOnError(status);
		IRGenerator result = (IRGenerator)context.createProductionNodeObject(handle.value, NodeType.IR);
		NativeMethods.xnProductionNodeRelease(handle.value);
		return result;
	}

	/**
	 * Constructor, makes a query for an IR generator in the given context.  Discards any enumeration errors.
	 * @param context OpenNI Context to search for a sensor in
	 * @param query Query object to filter search results
	 * @return Resulting IR Generator
	 * @throws GeneralException This function opens a hardware sensor, so exceptions are possible
	 */
	public static IRGenerator create(Context context, Query query) throws GeneralException
	{
		return create(context, query, null);
	}

	/** 
	 * Performs a query for an IR Generator in the given OpenNI context. 
	 * @param context OpenNI Context to look for an IR Generator in
	 * @return Resulting IR Generator
	 * @throws GeneralException
	 */
	public static IRGenerator create(Context context) throws GeneralException
	{
		return create(context, null, null);
	}
	
	/**
	 * Returns most recent IRMap output from the IRGenerator
	 * @return Most recent IRMap
	 * @throws GeneralException We are talking to a hardware sensor, so exceptions are possible
	 */
	public IRMap getIRMap() throws GeneralException
	{
		int frameID = getFrameID();
		
		if ((this.currIRMap == null) || (this.currIRMapFrameID != frameID))
		{
			long ptr = NativeMethods.xnGetIRMap(toNative());
			MapOutputMode mode = getMapOutputMode();
			this.currIRMap = new IRMap(ptr, mode.getXRes(), mode.getYRes());
			this.currIRMapFrameID = frameID; 
		}

		return this.currIRMap;
	}
	
	/** 
	 * Retrieves the most recent IRMetaData object from the Generator
	 * @param IRMD Object of type IRMetaData to store the result in
	 */
	public void getMetaData(IRMetaData IRMD)
	{
		NativeMethods.xnGetIRMetaData(this.toNative(), IRMD);
	}

	/**
	 * Same as getMetaData, except this function also creates the IRMetaData to 
	 * hold the result.
	 * @return resulting copy of IRMetaData
	 */
	public IRMetaData getMetaData()
	{
		IRMetaData IRMD = new IRMetaData();
		getMetaData(IRMD);
		return IRMD;
	}

	private IRMap currIRMap;
	private int currIRMapFrameID;
}
